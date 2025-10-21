plugins {
    id("org.springframework.boot") version "4.0.0-M3" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("java")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        // BOM Spring Boot
        implementation(platform("org.springframework.boot:spring-boot-dependencies:4.0.0-M3"))

        // Lombok pour tous
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        // Dépendances de test communes
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.assertj:assertj-core")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

// --------------------- MODULE DOMAIN ---------------------
project(":domain") {
    // module simple, pas de dépendances supplémentaires
}

// --------------------- MODULE APPLICATION ---------------------
project(":application") {
    dependencies {
        implementation(project(":domain"))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

// --------------------- MODULE INFRASTRUCTURE ---------------------
project(":infrastructure") {
    apply(plugin = "org.springframework.boot")

    java {
        withSourcesJar()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-restclient")
        implementation("org.springframework.boot:spring-boot-starter-batch")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        runtimeOnly("com.h2database:h2")
        implementation("org.springframework.boot:spring-boot-starter-actuator")

        implementation(project(":application"))
        implementation(project(":domain"))

        // SpringDoc OpenAPI
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.5.0")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

        // Lombok
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        // Test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        mainClass.set("org.kata.payment.infrastructure.PaymentApplication")
        archiveFileName.set("payment.jar")
    }
}

// --------------------- MODULE VALIDATION-TEST ---------------------
project(":validation-test") {
    dependencies {
        implementation(project(":infrastructure"))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.cucumber:cucumber-java:7.30.0")
        testImplementation("io.cucumber:cucumber-spring:7.30.0")
        testImplementation("io.cucumber:cucumber-junit-platform-engine:7.30.0")
        testImplementation("org.junit.platform:junit-platform-suite")
    }

    tasks.test {
        systemProperty("cucumber.plugin", "pretty, json:build/test-results/cucumber.json")
        systemProperty("cucumber.glue", "org.kata.payment")
        systemProperty("cucumber.features", "src/test/resources/features")
    }
}


// --------------------- MODULE PERF-TEST ---------------------
project(":perf-test") {
    dependencies {
        testImplementation("io.gatling.highcharts:gatling-charts-highcharts:3.11.4")
        testImplementation("io.gatling:gatling-test-framework:3.11.4")
    }

    tasks.register<JavaExec>("gatling") {
        group = "verification"
        description = "Run Gatling simulation"
        classpath = sourceSets["test"].runtimeClasspath
        mainClass.set("io.gatling.app.Gatling")
        args = listOf(
            "-s", "org.kata.simulation.PaymentSimulation",
            "-rf", "${buildDir}/gatling-results"
        )
    }
}

dependencies {
    implementation(project(":infrastructure"))
    testImplementation(project(":domain"))
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
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

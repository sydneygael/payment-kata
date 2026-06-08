dependencies {
    testImplementation("io.gatling.highcharts:gatling-charts-highcharts:3.11.4")
    testImplementation("io.gatling:gatling-test-framework:3.11.4")
}

tasks.test {
    failOnNoDiscoveredTests = false
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

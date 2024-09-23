plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("org.graalvm.polyglot:polyglot:24.1.0")
    implementation("org.graalvm.polyglot:python:24.1.0")

    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    // Define the main class for the application.
    mainClass = "com.example.App"
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

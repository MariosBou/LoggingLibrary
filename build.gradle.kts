plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // SLF4J API
    implementation("org.slf4j:slf4j-api:1.7.36")

    // Log4j2 bindings
    implementation("org.apache.logging.log4j:log4j-core:2.21.0")
    implementation("org.apache.logging.log4j:log4j-api:2.21.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.21.0")

    implementation("org.apache.logging.log4j:log4j-layout-template-json:2.21.0")

}

tasks.test {
    useJUnitPlatform()
}

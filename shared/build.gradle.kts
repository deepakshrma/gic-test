plugins {
    alias(libs.plugins.kotlin.jvm)
    kotlin("plugin.serialization") version "2.1.0"
}

group = "com.gic.cinemas"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.1")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.17")
}

tasks.test {
    useJUnitPlatform()
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
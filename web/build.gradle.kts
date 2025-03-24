//val kotlin_version: String by project
//val logback_version: String by project

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("io.ktor.plugin") version "3.1.1"
    kotlin("plugin.serialization") version "2.1.0"
    application
}

group = "com.gic.cinemas"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    api(project(":shared"))
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-host-common")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-config-yaml")
//    implementation("io.ktor:ktor-gson")
    testImplementation("io.ktor:ktor-server-test-host")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.1.0")
}

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
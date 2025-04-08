import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    application
    kotlin("jvm") version "2.0.10"
    alias(libs.plugins.org.somda.repository.collection)
}

group = "org.ornet"
version = "2.2.1"

val jdkVersion: JvmTarget = JvmTarget.JVM_17

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.org.somda.mdib.dsl)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(jdkVersion.target)
    }
}

kotlin {
    compilerOptions {
        jvmTarget = jdkVersion
    }
}

application {
    mainClass.set("MainKt")
}
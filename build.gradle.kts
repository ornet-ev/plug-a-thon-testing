import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    application
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.serialization") version "2.2.20"
    alias(libs.plugins.org.somda.repository.collection)
}

group = "org.ornet"
version = "2.1.1"

val jdkVersion: JvmTarget = JvmTarget.JVM_17

repositories {
    mavenCentral()
    // snapshots
    maven {
        url = uri("https://central.sonatype.com/repository/maven-snapshots")
    }
}

dependencies {
    implementation(libs.org.somda.mdib.dsl.biceps)
    implementation(libs.org.somda.mdib.dsl.ieee.nomenclature)
    implementation(libs.de.sven.jacobs.lorem.ipsum)
    implementation(libs.com.akuleshov7.ktoml.core)
    implementation(libs.com.akuleshov7.ktoml.file)
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
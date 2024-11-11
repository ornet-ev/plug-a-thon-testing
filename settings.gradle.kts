pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://gitlab.com/api/v4/projects/60445622/packages/maven")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "plug-a-thon-mdibs"


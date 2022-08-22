enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.kryptonmc.org/releases")
        maven("https://repo.velocitypowered.com/snapshots/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.kryptonmc.org/snapshots")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://jitpack.io")
    }
    versionCatalogs {
        create("global") {
            from(files("gradle/global.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven("https://repo.spongepowered.org/repository/maven-public/")
        gradlePluginPortal()
    }
}

rootProject.name = "krypton"

include("api")
include("server")
include("generators")
include("annotation-processor")

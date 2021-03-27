import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.31"
}

group = "org.kryptonmc"
version = "0.15"

rootProject.extra["globalVersion"] = project.version

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven("https://libraries.minecraft.net")
    }

    dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")

        api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
        api("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.1.0")

        api("net.kyori:adventure-api:4.6.0") {
            exclude("org.checkerframework", "checker-qual")
        }
        api("net.kyori:adventure-extra-kotlin:4.6.0") {
            exclude("org.checkerframework", "checker-qual")
        }

        api("com.mojang:brigadier:1.0.17")
        api("me.bardy:admiral:1.1")

        api("org.apache.logging.log4j:log4j-api:2.14.1")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
                "-Xopt-in=kotlin.ExperimentalUnsignedTypes",
                "-Xinline-classes"
            )
        }
    }
}
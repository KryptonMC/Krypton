import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
    id("org.jetbrains.dokka") version "1.4.30"
}

group = "org.kryptonmc"
version = "0.18.2"

rootProject.extra["globalVersion"] = project.version

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jetbrains.dokka")

    repositories {
        mavenCentral()
        maven("https://libraries.minecraft.net")
        jcenter()
    }

    dependencies {
        api(kotlin("stdlib"))
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")

        api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
        api("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.1.0")

        api("net.kyori:adventure-api:4.7.0")
        api("net.kyori:adventure-extra-kotlin:4.7.0")

        api("com.mojang:brigadier:1.0.17")

        api("org.apache.logging.log4j:log4j-api:2.14.1")
    }

    configurations.all {
        exclude("org.checkerframework", "checker-qual")
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

    task<Jar>("sourcesJar") {
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }

    task<Jar>("javadocJar") {
        from(tasks["dokkaJavadoc"])
        archiveClassifier.set("javadoc")
    }
}
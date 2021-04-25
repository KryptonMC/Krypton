import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.kryptonmc.krypton.Versions
import org.kryptonmc.krypton.adventure
import org.kryptonmc.krypton.dependsOn
import org.kryptonmc.krypton.kotlinx
import org.kryptonmc.krypton.log4j

plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
    id("org.jetbrains.dokka") version "1.4.30"
    id("info.solidsoft.pitest") version "1.6.0"
    `maven-publish`
    signing
}

group = "org.kryptonmc"
version = "0.18.5"

rootProject.extra["globalVersion"] = project.version

allprojects {
    repositories {
        mavenCentral()
        maven("https://libraries.minecraft.net")
        jcenter()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "info.solidsoft.pitest")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    dependencies {
        api(kotlin("stdlib"))
        api(kotlinx("coroutines-core", Versions.COROUTINES))

        api(kotlinx("serialization-json", Versions.SERIALIZATION))
        api(kotlinx("serialization-hocon", Versions.SERIALIZATION))

        api(adventure("api"))
        api(adventure("extra-kotlin"))

        api("com.mojang:brigadier:1.0.17")

        api(log4j("api"))

        // Testing
        testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}")
        testImplementation("org.junit.platform:junit-platform-runner:1.7.1")
        testImplementation(kotlin("test-junit5"))
        testImplementation("io.mockk:mockk:1.10.6")
    }

    configurations.all {
        exclude("org.checkerframework", "checker-qual")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
                freeCompilerArgs = listOf(
                    "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
                    "-Xopt-in=kotlin.ExperimentalUnsignedTypes",
                    "-Xinline-classes",
                    "-Xjvm-default=enable"
                )
            }
        }
        withType<Test> {
            useJUnitPlatform()
        }
    }

    pitest {
        junit5PluginVersion.set("0.12")
        avoidCallsTo.set(setOf("kotlin.jvm.internal"))
        mutators.set(setOf("STRONGER"))
        targetClasses.set(setOf("org.kryptonmc.*"))
        targetTests.set(setOf("org.kryptonmc.*"))
        threads.set(Runtime.getRuntime().availableProcessors())
        outputFormats.set(setOf("XML", "HTML"))
        excludedMethods.set(setOf("equals", "hashCode", "toString"))
    }

    task<Jar>("sourcesJar") {
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }

    task<Jar>("javadocJar") {
        from(tasks["dokkaJavadoc"])
        archiveClassifier.set("javadoc")
    }

    tasks["build"] dependsOn tasks["test"]
}

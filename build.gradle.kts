import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.GradleExternalDocumentationLinkBuilder
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.kryptonmc.krypton.Versions
import org.kryptonmc.krypton.adventure
import org.kryptonmc.krypton.dependsOn
import org.kryptonmc.krypton.junit
import org.kryptonmc.krypton.log4j

plugins {
    kotlin("jvm") version "1.5.0"
    id("org.jetbrains.dokka") version "1.4.30"
    id("info.solidsoft.pitest") version "1.6.0"
    id("org.cadixdev.licenser") version "0.6.0" apply false
    `maven-publish`
    signing

    // SlimJar
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    id("io.github.slimjar") version "1.1.2" apply false
}

allprojects {
    group = "org.kryptonmc"
    version = "0.20.1"

    repositories {
        mavenCentral()
        maven("https://libraries.minecraft.net/")
        jcenter()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "info.solidsoft.pitest")
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "io.github.slimjar")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    dependencies {
        testImplementation(junit("jupiter", "api"))
        testRuntimeOnly(junit("jupiter", "engine"))
        testImplementation(junit("platform", "runner", "1.7.1"))
        testImplementation(kotlin("test-junit5"))
        testImplementation("io.mockk:mockk:${Versions.MOCKK}")
        testRuntimeOnly("net.bytebuddy:byte-buddy:${Versions.BYTE_BUDDY}")

        // Duplicated test dependencies because SJ doesn't add them by default :(
        testImplementation("com.google.guava:guava:${Versions.GUAVA}")
        testApi("com.mojang:brigadier:${Versions.BRIGADIER}")
        testApi(adventure("api"))
        testApi(adventure("extra-kotlin"))
        testApi(log4j("api"))
    }

    configurations.all {
        exclude("org.checkerframework", "checker-qual")
        exclude("net.kyori", "adventure-bom")
    }

    val compiler = javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(16))
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "16"
                freeCompilerArgs = listOf("-Xjvm-default=all")
                jdkHome = compiler.get().metadata.installationPath.asFile.absolutePath
            }
        }
        withType<Test> {
            useJUnitPlatform()
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(16))
        }
    }

    pitest {
        junit5PluginVersion.set("0.12")
        avoidCallsTo.set(setOf(
            "kotlin.jvm.internal",
            "java.util.logging",
            "org.slf4j",
            "org.apache.logging.log4j"
        ))
        mutators.set(setOf("STRONGER"))
        targetClasses.set(setOf("org.kryptonmc.*"))
        targetTests.set(setOf("org.kryptonmc.*"))
        threads.set(Runtime.getRuntime().availableProcessors())
        outputFormats.set(setOf("XML", "HTML"))
        excludedMethods.set(setOf("equals", "hashCode", "toString", "emptyList"))
    }

    tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
        dokkaSourceSets {
            named("main") {
                fun adventure(module: String) = Action<GradleExternalDocumentationLinkBuilder> {
                    val baseURL = "https://jd.adventure.kyori.net/$module/${Versions.ADVENTURE}/"
                    url.set(uri(baseURL).toURL())
                    packageListUrl.set(uri("${baseURL}element-list").toURL())
                }

                externalDocumentationLink(adventure("api"))
                externalDocumentationLink(adventure("key"))
                externalDocumentationLink(adventure("nbt"))
                externalDocumentationLink(adventure("text-serializer-gson"))
                externalDocumentationLink(adventure("text-serializer-legacy"))
                externalDocumentationLink(adventure("text-serializer-plain"))
            }
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

    tasks["build"] dependsOn tasks["test"]
}

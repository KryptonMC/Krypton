import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.cadixdev.licenser")
}

repositories {
    maven("https://repo.kryptonmc.org/releases")
    maven("https://repo.velocitypowered.com/snapshots/")
    jcenter()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.platform", "junit-platform-runner", Versions.JUNIT_PLATFORM_RUNNER)
    testImplementation("io.mockk", "mockk", Versions.MOCKK)
    testRuntimeOnly("net.bytebuddy", "byte-buddy", Versions.BYTEBUDDY)
}

configurations.all {
    exclude("org.checkerframework", "checker-qual")
    exclude("junit")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "16"
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

tasks["build"].dependsOn(tasks["test"])

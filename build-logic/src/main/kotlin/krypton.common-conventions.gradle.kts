import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("net.kyori.indra")
    id("org.cadixdev.licenser")
    jacoco
}

configurations.all {
    exclude("org.checkerframework", "checker-qual")
    exclude("junit")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

indra {
    javaVersions {
        target(17)
    }

    github("KryptonMC", "Krypton") {
        ci(true)
    }
    gpl3OnlyLicense()
}

tasks {
    jacocoTestReport {
        dependsOn(test)
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

tasks["build"].dependsOn(tasks["test"])

license {
    header(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine(false)
}

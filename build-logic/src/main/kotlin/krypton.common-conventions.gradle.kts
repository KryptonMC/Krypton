import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("krypton.base-conventions")
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

jacoco.toolVersion = "0.8.7"

tasks["build"].dependsOn(tasks.test)

tasks {
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjvm-default=all", "-verbose")
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

license {
    header(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine(false)
}

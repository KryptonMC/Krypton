import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("krypton.basic-conventions")
    id("krypton.base-conventions")
    id("net.kyori.indra")
    jacoco
}

configurations.all { exclude("junit") }

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

tasks {
    build {
        dependsOn(test)
    }
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

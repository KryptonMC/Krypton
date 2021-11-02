import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.cadixdev.licenser")
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

license {
    header(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine(false)
}

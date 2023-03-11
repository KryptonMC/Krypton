import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("krypton.basic-conventions")
}

configurations.all {
    exclude("junit")
    exclude("org.checkerframework", "checker-qual")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    build {
        dependsOn(test)
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

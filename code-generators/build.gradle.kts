plugins {
    kotlin("jvm")
    id("org.cadixdev.licenser")
    `java-library`
    application
}

application.mainClass.set("org.kryptonmc.codegen.GeneratorsKt")

repositories {
    mavenCentral()
    maven("https://repo.bristermitten.me/repository/maven-public/")
}

dependencies {
    implementation("com.google.code.gson", "gson", Versions.GSON)
    implementation("me.bardy", "gson-kt", Versions.GSON_KT)
    implementation("com.squareup", "kotlinpoet", Versions.KOTLINPOET)
    implementation("org.apache.logging.log4j", "log4j-core", Versions.LOG4J)
    implementation("de.articdive", "articdata", Versions.MINECRAFT)
}

val compiler: Provider<JavaCompiler> = javaToolchains.compilerFor {
    languageVersion.set(JavaLanguageVersion.of(16))
}

tasks {
    run.configure {
        val separator = File.separator
        args = listOf(
            Versions.MINECRAFT,
            "resources",
            "${project.rootDir}${separator}api${separator}src${separator}generated${separator}kotlin"
        )
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "16"
            freeCompilerArgs = listOf("-Xjvm-default=all")
            jdkHome = compiler.get().metadata.installationPath.asFile.absolutePath
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

license {
    header(rootProject.resources.text.fromFile("HEADER.txt"))
    newLine(false)
}

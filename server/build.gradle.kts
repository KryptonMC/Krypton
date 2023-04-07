import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("krypton.api-conventions")
    id("io.gitlab.arturbosch.detekt")
    id("com.google.devtools.ksp")
    jacoco
}

dependencies {
    api(projects.api)
    implementation(projects.annotationProcessor)

    // Logging and console
    runtimeOnly(libs.log4j.core)
    implementation(libs.tca)
    runtimeOnly(libs.jline.jansi)

    // Data
    api(libs.dataConverter)
    api(libs.nbt)
    api(libs.nbt.kotlin)
    api(libs.serialization)
    api(libs.serialization.gson)
    api(libs.serialization.nbt)
    implementation(libs.articData)

    // Collections and caching
    implementation(libs.fastutil)
    implementation(libs.flare.fastutil)
    implementation(libs.caffeine)
    implementation(libs.jctools)

    // Miscellaneous
    implementation(libs.clikt)
    implementation(libs.bstats)
    implementation(libs.kotlinx.collections.immutable)
    compileOnly(projects.internalAnnotations)
    ksp(projects.internalAp)
    implementation(libs.reflections)
    implementation(libs.hydrazine)

    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.junit.platform.runner)
    testImplementation(libs.mockk)
    testImplementation(libs.jimfs)
    testRuntimeOnly(libs.bytebuddy)
    testImplementation(projects.internalAnnotations)
    testImplementation(libs.equalsVerifier)
    testImplementation(libs.jsonassert)
}

license {
    exclude(
        "**/*.properties",
        "**/*.conf",
        "**/*.json",
        // Velocity derivatives, with a special header
        "**/plugin/KryptonPluginManager.kt",
        "**/plugin/PluginClassLoader.kt",
        "**/plugin/PluginDependencies.kt",
        // Sponge derivatives, with a special header
        "**/console/BrigadierCompleter.kt",
        "**/console/BrigadierHighlighter.kt"
    )
}

tasks {
    withType<ProcessResources> {
        filesMatching("**/versions.properties") {
            val minecraftVersion = global.versions.minecraft.get()
            filter<ReplaceTokens>("tokens" to mapOf(
                "krypton" to project.version.toString(),
                "minecraft" to minecraftVersion,
                "data" to minecraftVersion.replace('.', '_')
            ))
        }
    }
    jacocoTestReport {
        dependsOn(test)
        reports {
            html.required.set(true)
            xml.required.set(true)
        }
    }
    build {
        dependsOn(test)
    }
    compileKotlin {
        compilerOptions.freeCompilerArgs.add("-Xjvm-default=all")
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

setupDetekt()

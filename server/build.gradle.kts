import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("io.gitlab.arturbosch.detekt")
    id("info.solidsoft.pitest")
    id("com.google.devtools.ksp")
}

dependencies {
    api(projects.api)
    implementation(projects.annotationProcessor)

    // Networking
    api(libs.netty.buffer)
    api(libs.netty.handler)
    api(libs.netty.transport)
    implementation(libs.netty.transport.native.epoll) {
        artifact {
            classifier = "linux-x86_64"
        }
    }
    implementation(libs.netty.transport.native.kqueue) {
        artifact {
            classifier = "osx-x86_64"
        }
    }
    implementation(libs.velocity.native)

    // Events
    implementation(libs.lmbda)

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
    pitest(libs.arcmutateKotlin)
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

pitest {
    pitestVersion.set(libs.versions.pitest)
    junit5PluginVersion.set("1.0.0")
    targetClasses.set(setOf("org.kryptonmc.api.*", "org.kryptonmc.krypton.*"))
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
}

setupDetekt()

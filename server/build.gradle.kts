import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("com.github.johnrengelman.shadow")
    `java-library`
    jacoco
}

dependencies {
    api(projects.api)
    implementation(projects.annotationProcessor)

    // Extra Kotlin stuff
    api(libs.kotlin.stdlib.jdk7)
    api(libs.kotlin.stdlib.jdk8)

    // Networking
    api(libs.bundles.netty)
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
    implementation(libs.event)
    implementation(libs.asm)

    // Logging and console
    runtimeOnly(libs.log4j.core)
    implementation(libs.tca)
    runtimeOnly(libs.jline.jansi)

    // Data
    api(libs.dataConverter)
    api(libs.nbt)
    implementation(libs.articData)

    // Miscellaneous
    implementation(libs.caffeine)
    implementation(libs.fastutil)
    implementation(libs.clikt)
    implementation(libs.bstats)
    implementation(libs.spark.common)

    testImplementation(libs.bundles.junit)
    testImplementation(libs.junit.platform.runner)
    testImplementation(libs.mockk)
    testRuntimeOnly(libs.bytebuddy)
}

tasks {
    jar {
        manifest {
            attributes("Main-Class" to "org.kryptonmc.krypton.KryptonKt")
            attributes("Implementation-Title" to "Krypton")
            attributes("Implementation-Version" to project.version.toString())
            attributes("Implementation-Vendor" to "KryptonMC")
            attributes("Multi-Release" to "true")
        }
    }
    withType<ShadowJar> {
        val buildNumber = System.getenv("BUILD_NUMBER")?.let { "-$it" }.orEmpty()
        archiveFileName.set("Krypton-${project.version}$buildNumber.jar")
        transform(Log4j2PluginsCacheFileTransformer::class.java)

        exclude("it/unimi/dsi/fastutil/booleans/**")
        exclude("it/unimi/dsi/fastutil/bytes/**")
        exclude("it/unimi/dsi/fastutil/chars/**")
        exclude("it/unimi/dsi/fastutil/floats/**")
        exclude("it/unimi/dsi/fastutil/io/**")
        exclude("it/unimi/dsi/fastutil/objects/*Reference*")
        exclude("it/unimi/dsi/fastutil/shorts/**")

        relocate("org.bstats", "org.kryptonmc.krypton.bstats")
    }
    withType<ProcessResources> {
        filter<ReplaceTokens>("tokens" to mapOf(
            "version" to project.version.toString(),
            "minecraft" to global.versions.minecraft.get(),
            "spark" to libs.versions.spark.get(),
            "data" to global.versions.minecraft.get().replace('.', '_')
        ))
    }
}

tasks["build"].dependsOn(tasks["shadowJar"])

jacoco {
    toolVersion = global.versions.jacoco.get()
}

license {
    exclude(
        "**/*.properties",
        "**/*.conf",
        "**/*.json",
        // Velocity derivatives, with a special header
        "**/plugin/KryptonEventManager.kt",
        "**/plugin/KryptonPluginContainer.kt",
        "**/plugin/KryptonPluginManager.kt",
        "**/plugin/PluginClassLoader.kt",
        "**/plugin/dependencies.kt",
        "**/plugin/loader/LoadedPluginDescription.kt",
        "**/plugin/loader/LoadedPluginDescriptionCandidate.kt",
        "**/plugin/loader/PluginLoader.kt",
        "**/scheduling/KryptonScheduler.kt",
        "**/util/bytebufs.kt"
    )
}

tasks.jacocoTestReport {
    sourceSets(project(":api").sourceSets.main.get())
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("io.gitlab.arturbosch.detekt")
}

sourceSets.main {
    java.srcDir("src/generated/kotlin")
}

dependencies {
    // Kotlin
    api(libs.kotlin.stdlib)
    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines)

    // Core
    api(libs.guava)
    api(libs.gson)
    api(libs.gsonKt)
    api(libs.commonsLang)
    api(libs.log4j.api)

    // Dependency injection
    api(libs.guice)
    api(libs.kotlinGuice)

    // Adventure
    api(libs.bundles.adventure)

    // Configurate
    api(libs.bundles.configurate)

    // Miscellaneous
    api(libs.velocity.brigadier)
    api(libs.math)
    api(libs.spark.api)
}

license {
    header(project.resources.text.fromFile("HEADER.txt"))
    newLine(false)
    exclude(
        // Velocity derivatives, with a special header
        "**/event/ComponentResult.kt",
        "**/event/EventHandler.kt",
        "**/event/EventManager.kt",
        "**/event/GenericResult.kt",
        "**/event/ResultedEvent.kt",
        "**/event/server/SetupPermissionsEvent.kt",
        "**/plugin/InvalidPluginException.kt",
        "**/plugin/PluginContainer.kt",
        "**/plugin/PluginDependency.kt",
        "**/plugin/PluginDescription.kt",
        "**/plugin/PluginManager.kt",
        "**/plugin/annotation/DataFolder.kt",
        "**/plugin/annotation/Dependency.kt",
        "**/plugin/annotation/Plugin.kt",
        "**/permission/PermissionFunction.kt",
        "**/permission/PermissionProvider.kt",
        "**/permission/Subject.kt",
        // Sponge derivatives, with a special header
        "**/world/rule/GameRules.kt"
    )
}

detekt {
    buildUponDefaultConfig = true
    config = files("$projectDir/config/detekt.yml")
    baseline = file("$projectDir/config/baseline.xml")
}

kotlin {
    explicitApi()
}

tasks {
    jar {
        manifest {
            attributes("Specification-Title" to "Krypton API")
            attributes("Specification-Vendor" to "KryptonMC")
            attributes("Specification-Version" to project.version.toString())
        }
    }
    withType<DokkaTask>().configureEach {
        dokkaSourceSets {
            named("main") {
                sequenceOf(
                    "api",
                    "key",
                    "text-serializer-gson",
                    "text-serializer-legacy",
                    "text-serializer-plain",
                    "serializer-configurate4"
                ).forEach { javadocLink("https://jd.adventure.kyori.net/$it/${libs.versions.adventure.get()}/") }
                externalDocumentationLink("https://logging.apache.org/log4j/log4j-${libs.versions.log4j.get()}/log4j-api/apidocs/")
                javadocLink("https://javadoc.io/doc/com.google.code.gson/gson/${libs.versions.gson.get()}/")
                javadocLink("https://google.github.io/guice/api-docs/${libs.versions.guice.get()}/javadoc/")
                externalDocumentationLink("https://commons.apache.org/proper/commons-lang/apidocs/")
                javadocLink("https://configurate.aoeu.xyz/${libs.versions.configurate.get()}/apidocs/")
            }
        }
    }
    detekt.configure {
        reports {
            html.required.set(true)
            xml.required.set(true)
        }
    }
}

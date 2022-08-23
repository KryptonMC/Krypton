plugins {
    id("io.gitlab.arturbosch.detekt")
}

sourceSets.main {
    java.srcDir("src/generated/kotlin")
}

dependencies {
    // Kotlin
    api(libs.bundles.kotlin)
    api(libs.kotlinx.coroutines)
    api(libs.kotlinx.collections.immutable)

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
    dokkaTasks.configureEach {
        configureSourceSets {
            modernJavadocLink("https://guava.dev/releases/${libs.versions.guava.get()}/api/docs/")
            modernJavadocLink("https://javadoc.io/doc/com.google.code.gson/gson/${libs.versions.gson.get()}/")
            externalDocumentationLink("https://commons.apache.org/proper/commons-lang/apidocs/")
            externalDocumentationLink("https://logging.apache.org/log4j/log4j-${libs.versions.log4j.get()}/log4j-api/apidocs/")
            modernJavadocLink("https://google.github.io/guice/api-docs/${libs.versions.guice.get()}/javadoc/")
            sequenceOf("api", "key", "text-serializer-gson", "text-serializer-legacy", "text-serializer-plain", "serializer-configurate4")
                .forEach { modernJavadocLink("https://jd.adventure.kyori.net/$it/${libs.versions.adventure.get()}/") }
            modernJavadocLink("https://configurate.aoeu.xyz/${libs.versions.configurate.get()}/apidocs/")
        }
    }
    detekt.configure {
        reports {
            html.required.set(true)
            xml.required.set(true)
        }
    }
}

applySpecJarMetadata("org.kryptonmc.api", "Krypton API")

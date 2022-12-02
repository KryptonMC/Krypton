plugins {
    id("io.gitlab.arturbosch.detekt")
    id("com.google.devtools.ksp")
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
    api(libs.commonsLang)
    api(libs.log4j.api)

    // Dependency injection
    api(libs.guice)
    api(libs.kotlinGuice)

    // Adventure
    api(libs.adventure.api)
    api(libs.adventure.extraKotlin)
    api(libs.adventure.minimessage)
    api(libs.adventure.serializer.gson)
    api(libs.adventure.serializer.legacy)
    api(libs.adventure.serializer.plain)
    api(libs.adventure.serializer.configurate)

    // Configurate
    api(libs.configurate.core)
    api(libs.configurate.gson)
    api(libs.configurate.hocon)
    api(libs.configurate.yaml)
    api(libs.configurate.extraKotlin)

    // Miscellaneous
    api(libs.brigadier)
    compileOnly(libs.annotations)
    compileOnly(projects.internalAnnotations)
    ksp(projects.internalAp)
}

license {
    header(project.resources.text.fromFile("HEADER.txt"))
    exclude(
        // Velocity derivatives, with a special header
        "**/event/ComponentResult.kt",
        "**/event/Continuation.kt",
        "**/event/EventHandler.kt",
        "**/event/EventManager.kt",
        "**/event/EventTask.kt",
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
}

applySpecJarMetadata("org.kryptonmc.api", "Krypton API")
setupDetekt()

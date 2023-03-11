import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("org.jetbrains.dokka")
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
    exclude(
        // Velocity derivatives, with a special header
        "**/plugin/InvalidPluginException.kt",
        "**/plugin/PluginContainer.kt",
        "**/plugin/PluginDependency.kt",
        "**/plugin/PluginDescription.kt",
        "**/plugin/PluginManager.kt",
        "**/plugin/annotation/DataFolder.kt",
        "**/plugin/annotation/Dependency.kt",
        "**/plugin/annotation/Plugin.kt",
        "**/permission/PermissionFunction.kt",
        "**/permission/Subject.kt"
    )
}

kotlin {
    explicitApi()
}

tasks {
    create<Jar>("dokkaJavadocJar") {
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc)
    }
    withType<DokkaTask> {
        dokkaSourceSets.named("main").configure {
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

publishing.publications.configureEach {
    if (this is MavenPublication) artifact(tasks["dokkaJavadocJar"])
}

configureJarMetadata("org.kryptonmc.api") {
    put("Specification-Title", "Krypton API")
    put("Specification-Vendor", "KryptonMC")
    put("Specification-Version", version.toString())
}
setupDetekt()

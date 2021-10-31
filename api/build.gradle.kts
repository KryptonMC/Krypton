import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("krypton.common")
    id("org.jetbrains.dokka")
    id("io.gitlab.arturbosch.detekt") version "1.18.1"
    `maven-publish`
    signing
}

sourceSets.main {
    java.srcDir("src/generated/kotlin")
}

dependencies {
    // BOMs
    api(platform("net.kyori:adventure-bom:${Versions.ADVENTURE}"))
    api(platform("org.spongepowered:configurate-bom:${Versions.CONFIGURATE}"))
    api(platform("org.apache.logging.log4j:log4j-bom:${Versions.LOG4J}"))

    // Kotlin
    api(kotlin("stdlib"))
    api(kotlin("reflect"))
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Versions.COROUTINES)

    // Core
    api("com.google.guava", "guava", Versions.GUAVA) {
        exclude("com.google.code.findbugs", "jsr305")
        exclude("com.google.j2objc", "j2objc-annotations")
    }
    api("com.google.code.gson", "gson", Versions.GSON)
    api("me.bardy", "gson-kt", Versions.GSON_KT)
    api("org.apache.commons", "commons-lang3", Versions.COMMONS_LANG)
    api("org.apache.logging.log4j", "log4j-api")

    // Dependency injection
    api("com.google.inject", "guice", Versions.GUICE) {
        exclude("com.google.code.findbugs", "jsr305")
        exclude("javax.inject", "javax.inject")
    }
    api("dev.misfitlabs.kotlinguice4", "kotlin-guice", Versions.KOTLIN_GUICE)

    // Adventure
    api("net.kyori", "adventure-api")
    api("net.kyori", "adventure-extra-kotlin")
    api("net.kyori", "adventure-text-serializer-gson")
    api("net.kyori", "adventure-text-serializer-legacy")
    api("net.kyori", "adventure-text-serializer-plain")

    // Configurate
    api("org.spongepowered", "configurate-core")
    api("org.spongepowered", "configurate-gson")
    api("org.spongepowered", "configurate-hocon")
    api("org.spongepowered", "configurate-yaml")
    api("org.spongepowered", "configurate-extra-kotlin")
    api("org.spongepowered", "configurate-extra-guice")

    // Miscellaneous
    api("com.velocitypowered", "velocity-brigadier", Versions.BRIGADIER)
    api("org.spongepowered", "math", Versions.MATH)
}

task<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

task<Jar>("javadocJar") {
    from(tasks["dokkaJavadoc"])
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        krypton(project)
    }

    publications {
        create<MavenPublication>("kryptonApi") {
            artifactId = "krypton-api"

            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set("Krypton API")
                description.set("The official API for Krypton")
                url.set("https://www.kryptonmc.org")
                inceptionYear.set("2021")
                packaging = "jar"
                kryptonDetails()
            }
        }
    }
}

signing {
    sign(publishing.publications["kryptonApi"])
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

    reports {
        html.enabled = true
        xml.enabled = true
    }
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
                ).forEach { javadocLink("https://jd.adventure.kyori.net/$it/${Versions.ADVENTURE}/") }
                externalDocumentationLink("https://logging.apache.org/log4j/log4j-${Versions.LOG4J}/log4j-api/apidocs/")
                javadocLink("https://javadoc.io/doc/com.google.code.gson/gson/${Versions.GSON}/")
                javadocLink("https://google.github.io/guice/api-docs/${Versions.GUICE}/javadoc/")
                externalDocumentationLink("https://commons.apache.org/proper/commons-lang/apidocs/")
                externalDocumentationLink("https://commons.apache.org/proper/commons-text/apidocs/")
                javadocLink("https://configurate.aoeu.xyz/${Versions.CONFIGURATE}/apidocs/")
            }
        }
    }
}

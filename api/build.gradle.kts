import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("krypton.common")
    id("org.jetbrains.dokka")
}

dependencies {
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
    api("org.apache.commons", "commons-text", Versions.COMMONS_TEXT)

    // Dependency injection
    api("com.google.inject", "guice", Versions.GUICE) {
        exclude("com.google.code.findbugs", "jsr305")
        exclude("javax.inject", "javax.inject")
    }
    api("dev.misfitlabs.kotlinguice4", "kotlin-guice", Versions.KOTLIN_GUICE)

    // Adventure
    api("net.kyori", "adventure-api")
    api("net.kyori", "adventure-extra-kotlin")
    api("net.kyori", "adventure-serializer-configurate4")

    // Configurate
    api("org.spongepowered", "configurate-core")
    api("org.spongepowered", "configurate-gson")
    api("org.spongepowered", "configurate-hocon")
    api("org.spongepowered", "configurate-yaml")
    api("org.spongepowered", "configurate-extra-kotlin")
    api("org.spongepowered", "configurate-extra-guice")

    // Miscellaneous
    api("com.mojang", "brigadier", Versions.BRIGADIER)
    api("org.spongepowered", "math", Versions.MATH)
    api("org.apache.logging.log4j", "log4j-api")
}

pitest {
    excludedClasses.set(setOf("org.kryptonmc.krypton.api.effect.particle.*ParticleEffectBuilder"))
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
        maven {
            val snapshots = uri("https://repo.bristermitten.me/repository/maven-snapshots/")
            val releases = uri("https://repo.bristermitten.me/repository/maven-releases/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshots else releases

            credentials {
                username = project.property("maven.username")!!.toString()
                password = project.property("maven.password")!!.toString()
            }
        }
    }

    publications {
        create<MavenPublication>("mavenKotlin") {
            artifactId = "api"

            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set("Krypton API")
                description.set("The official API for Krypton")
                url.set("https://www.kryptonmc.org")
                inceptionYear.set("2021")
                packaging = "jar"

                developers {
                    developer("bombardygamer", "Callum Seabrook", "callum.seabrook@prevarinite.com", "Europe/London", "Lead Developer")
                    developer("esophose", "Nicole Barningham", "esophose@gmail.com", "America/Boise", "Developer")
                }

                contributors {
                    contributor("Brandon Li", "brandonli2006ma@gmail.com", "America/New_York")
                }

                organization {
                    name.set("KryptonMC")
                    url.set("https://github.com/KryptonMC")
                }

                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/KryptonMC/Krypton/issues")
                }

                ciManagement {
                    system.set("Jenkins")
                    url.set("https://ci.kryptonmc.org/job/Krypton")
                }

                distributionManagement {
                    downloadUrl.set("https://ci.kryptonmc.org/job/Krypton/lastSuccessfulBuild/Krypton")
                }

                scm {
                    connection.set("scm:git:git://github.com/KryptonMC/Krypton.git")
                    developerConnection.set("scm:git:ssh://github.com:KryptonMC/Krypton.git")
                    url.set("https://github.com/KryptonMC/Krypton")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenKotlin"])
}

license {
    header.set(project.resources.text.fromFile("HEADER.txt"))
    newLine.set(false)
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            sequenceOf("api", "key", "nbt", "text-serializer-gson", "text-serializer-legacy", "text-serializer-plain", "serializer-configurate4").forEach {
                javadocLink("https://jd.adventure.kyori.net/$it/${Versions.ADVENTURE}/")
            }
            externalDocumentationLink("https://logging.apache.org/log4j/log4j-${Versions.LOG4J}/log4j-api/apidocs/")
            javadocLink("https://javadoc.io/doc/com.google.code.gson/gson/${Versions.GSON}/")
            javadocLink("https://google.github.io/guice/api-docs/${Versions.GUICE}/javadoc/")
            externalDocumentationLink("https://commons.apache.org/proper/commons-lang/apidocs/")
            externalDocumentationLink("https://commons.apache.org/proper/commons-text/apidocs/")
            javadocLink("https://configurate.aoeu.xyz/${Versions.CONFIGURATE}/apidocs/")
        }
    }
}

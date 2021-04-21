import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "6.1.0"
    application
    `maven-publish`
    signing
}

//application.mainClass.set("org.kryptonmc.krypton.KryptonKt")
application.mainClassName = "org.kryptonmc.krypton.KryptonKt"

object Versions {

    const val NETTY = "4.1.63.Final"
    const val ADVENTURE = "4.7.0"
}

dependencies {
    api(project(":krypton-api"))

    // Netty
    api("io.netty:netty-buffer:${Versions.NETTY}")
    api("io.netty:netty-handler:${Versions.NETTY}")
    api("io.netty:netty-transport:${Versions.NETTY}")

    // Netty native transport
    implementation("io.netty:netty-transport-native-epoll:${Versions.NETTY}")
    implementation("io.netty:netty-transport-native-kqueue:${Versions.NETTY}")
    implementation("io.netty.incubator:netty-incubator-transport-native-io_uring:0.0.5.Final")

    // Adventure
    api("net.kyori:adventure-text-serializer-gson:${Versions.ADVENTURE}")
    api("net.kyori:adventure-text-serializer-legacy:${Versions.ADVENTURE}")
    api("net.kyori:adventure-text-serializer-plain:${Versions.ADVENTURE}")
    api("net.kyori:adventure-nbt:${Versions.ADVENTURE}")

    // Logging
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.14.1")
    api("net.minecrell:terminalconsoleappender:1.2.0")
    runtimeOnly("org.jline:jline-terminal-jansi:3.19.0")

    // HTTP
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    api("com.squareup.okhttp3:okhttp:4.9.1")

    // Caching
    api("com.github.ben-manes.caffeine:caffeine:3.0.1")
    api("it.unimi.dsi:fastutil-core:8.5.4")

    // CLI
    implementation("com.github.ajalt.clikt:clikt:3.0.1")
}

tasks {
    withType<ShadowJar> {
        archiveFileName.set("Krypton-${rootProject.extra["globalVersion"]}.jar")
        transform(Log4j2PluginsCacheFileTransformer::class.java)

        exclude("it/unimi/dsi/fastutil/booleans/*.class")
        exclude("it/unimi/dsi/fastutil/bytes/*.class")
        exclude("it/unimi/dsi/fastutil/chars/*.class")
        exclude("it/unimi/dsi/fastutil/doubles/*.class")
        exclude("it/unimi/dsi/fastutil/floats/*.class")
        exclude("it/unimi/dsi/fastutil/io/*.class")
        exclude("it/unimi/dsi/fastutil/shorts/*.class")
    }
    withType<ProcessResources> {
        val tokens = mapOf("version" to rootProject.extra["globalVersion"])
        filter<ReplaceTokens>("tokens" to tokens)
    }
}

publishing {
    repositories {
        maven {
            val releasesRepoURL = uri("https://repo.bristermitten.me/repository/maven-releases/")
            val snapshotsRepoURL = uri("https://repo.bristermitten.me/repository/maven-snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoURL else releasesRepoURL

            credentials {
                username = if (project.hasProperty("maven.username")) project.property("maven.username").toString() else System.getenv("MAVEN_USERNAME")
                password = if (project.hasProperty("maven.password")) project.property("maven.password").toString() else System.getenv("MAVEN_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenKotlin") {
            group = "org.kryptonmc"
            artifactId = "krypton"
            version = rootProject.extra["globalVersion"].toString()

            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set("Krypton")
                description.set("The fast and lightweight Minecraft server written in Kotlin!")
                url.set("https://www.kryptonmc.org")
                inceptionYear.set("2021")

                packaging = "jar"

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("bombardygamer")
                        name.set("Callum Seabrook")
                        email.set("callum.seabrook@prevarinite.com")
                        timezone.set("Europe/London")

                        organization.set("KryptonMC")
                        organizationUrl.set("https://github.com/KryptonMC")

                        roles.set(setOf("Lead Developer"))
                    }

                    developer {
                        id.set("knightzmc")
                        name.set("Alexander Wood")
                        email.set("alexwood2403@gmail.com")
                        timezone.set("Europe/London")

                        organization.set("KryptonMC")
                        organizationUrl.set("https://github.com/KryptonMC")

                        roles.set(setOf("Developer"))
                    }

                    developer {
                        id.set("esophose")
                        name.set("Nicole Barningham")
                        email.set("esophose@gmail.com")
                        timezone.set("America/Boise")

                        organization.set("KryptonMC")
                        organizationUrl.set("https://github.com/KryptonMC")

                        roles.set(setOf("Developer"))
                    }
                }

                contributors {
                    contributor {
                        name.set("Brandon Li")
                        email.set("brandonli2006ma@gmail.com")
                        timezone.set("America/New_York")
                        roles.set(setOf("Contributor"))
                    }
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

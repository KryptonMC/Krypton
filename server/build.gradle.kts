import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import org.apache.tools.ant.filters.ReplaceTokens
import org.kryptonmc.krypton.Versions
import org.kryptonmc.krypton.adventure
import org.kryptonmc.krypton.applyCommon
import org.kryptonmc.krypton.applyRepositories
import org.kryptonmc.krypton.log4j
import org.kryptonmc.krypton.netty

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "6.1.0"
    application
    `maven-publish`
    signing
}

//application.mainClass.set("org.kryptonmc.krypton.KryptonKt")
application.mainClassName = "org.kryptonmc.krypton.KryptonKt"

dependencies {
    api(project(":krypton-api"))

    // Netty
    api(netty("buffer"))
    api(netty("handler"))
    api(netty("transport"))

    // Netty native transport
    implementation(netty("transport-native-epoll"))
    implementation(netty("transport-native-kqueue"))
    implementation("io.netty.incubator:netty-incubator-transport-native-io_uring:0.0.5.Final")

    // Adventure
    api(adventure("text-serializer-gson"))
    api(adventure("text-serializer-legacy"))
    api(adventure("text-serializer-plain"))
    api(adventure("nbt"))

    // Logging
    runtimeOnly(log4j("core"))
    api("net.minecrell:terminalconsoleappender:1.2.0")
    runtimeOnly("org.jline:jline-terminal-jansi:3.19.0")

    // HTTP
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    api("com.squareup.okhttp3:okhttp:${Versions.OKHTTP}")

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

pitest {
    avoidCallsTo.set(setOf(
        "kotlin.jvm.internal",
        "org.apache.logging.log4j"
    ))
    excludedClasses.set(setOf(
        "org.kryptonmc.krypton.KryptonKt*",
        "org.kryptonmc.krypton.KryptonCLI*",
        "org.kryptonmc.krypton.KryptonServer*",
        "org.kryptonmc.krypton.NettyProcess*",
        "org.kryptonmc.krypton.WatchdogProcess*",
        "org.kryptonmc.krypton.auth.MojangUUIDSerializer*",
        "org.kryptonmc.krypton.auth.requests.SessionService*"
    ))
    excludedMethods.set(setOf(
        "write\$Self"
    ))
}

publishing {
    applyRepositories(project)
    publications {
        create<MavenPublication>("mavenKotlin") {
            applyCommon(project, "krypton")
            pom.applyCommon("Krypton", "The fast and lightweight Minecraft server written in Kotlin!")
        }
    }
}

signing {
    sign(publishing.publications["mavenKotlin"])
}

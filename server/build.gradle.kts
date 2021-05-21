import io.github.slimjar.task.SlimJar
import org.kryptonmc.krypton.Versions
import org.kryptonmc.krypton.adventure
import org.kryptonmc.krypton.applyCommon
import org.kryptonmc.krypton.applyRepositories
import org.kryptonmc.krypton.log4j
import org.kryptonmc.krypton.netty

plugins {
    id("org.cadixdev.licenser")
    `java-library`
    `maven-publish`
    signing
}

repositories {
    maven("https://repo.velocitypowered.com/snapshots/")
}

dependencies {
    compileOnlyApi(project(":krypton-api"))

    // Kotlin
    slimApi(kotlin("reflect"))
    slim(kotlin("stdlib-jdk7"))
    slim(kotlin("stdlib-jdk8"))

    // Netty
    slimApi(netty("buffer"))
    slimApi(netty("handler"))
    slimApi(netty("transport"))

    // Netty native transport
    slim(netty("transport-native-epoll"))
    slim(netty("transport-native-kqueue"))
    slim("io.netty.incubator:netty-incubator-transport-native-io_uring:0.0.5.Final")

    // Adventure
    slimApi(adventure("text-serializer-gson"))
    slimApi(adventure("text-serializer-legacy"))
    slimApi(adventure("text-serializer-plain"))
    slimApi(adventure("nbt"))

    // Logging
    slim(log4j("core"))
    slimApi("net.minecrell:terminalconsoleappender:1.2.0")
    slim("org.jline:jline-terminal-jansi:3.19.0")

    // HTTP
    slimApi("com.squareup.retrofit2:retrofit:${Versions.RETROFIT}")
    slimApi("com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}")
    slimApi("com.squareup.okhttp3:okhttp:${Versions.OKHTTP}")

    // Caching
    slimApi("com.github.ben-manes.caffeine:caffeine:3.0.1")
    slimApi("it.unimi.dsi:fastutil:8.5.4")

    // Miscellaneous
    slimApi("org.spongepowered:math:2.0.0")
    slim("com.github.ajalt.clikt:clikt:3.2.0")
    slim("org.bstats:bstats-base:2.2.0")
    implementation("com.velocitypowered:velocity-native:1.1.0-SNAPSHOT") {
        exclude("com.google.guava", "guava")
        exclude("io.netty", "netty-handler")
    }
}

tasks.withType<SlimJar> {
    shade = false
}

pitest {
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

license {
    header.set(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine.set(false)
    exclude("**/*.properties", "**/*.conf", "**/*.json")
}

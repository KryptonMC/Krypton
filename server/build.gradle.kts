import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("krypton.common")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow") version Versions.SHADOW
    `java-library`
    jacoco
    `maven-publish`
    signing
}

evaluationDependsOn(":api")

repositories {
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.kryptonmc.org/snapshots")
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":api"))
    implementation(project(":annotation-processor"))
    implementation(platform("io.netty:netty-bom:${Versions.NETTY}"))

    // Extra Kotlin stuff
    implementation(kotlin("stdlib-jdk7"))
    implementation(kotlin("stdlib-jdk8"))

    // Networking
    implementation("io.netty", "netty-buffer")
    implementation("io.netty", "netty-handler")
    implementation("io.netty", "netty-transport")

    // Extra natives for networking
    implementation("io.netty", "netty-transport-native-epoll")
    implementation("io.netty", "netty-transport-native-kqueue")
    implementation("com.velocitypowered", "velocity-native", Versions.VELOCITY_NATIVE)

    // Events
    implementation("net.kyori", "event-method-asm", Versions.EVENT)
    implementation("org.ow2.asm", "asm", Versions.ASM)

    // Logging and console
    runtimeOnly("org.apache.logging.log4j", "log4j-core")
    implementation("net.minecrell", "terminalconsoleappender", Versions.TCA)
    runtimeOnly("org.jline", "jline-terminal-jansi", Versions.JANSI)

    // Data
    implementation("ca.spottedleaf", "data-converter", Versions.DATA_CONVERTER)
    implementation("org.kryptonmc", "datafixerupper", Versions.DFU) // Slight performance enhanced version, courtesy of Paper
    implementation("org.kryptonmc", "nbt-common-jvm", Versions.NBT)
    implementation("com.github.Articdive", "ArticData", Versions.ARTICDATA)

    // Miscellaneous
    implementation("com.github.ben-manes.caffeine", "caffeine", Versions.CAFFEINE)
    implementation("it.unimi.dsi", "fastutil", Versions.FASTUTIL)
    implementation("com.github.ajalt.clikt", "clikt", Versions.CLIKT)
    implementation("org.bstats", "bstats-base", Versions.BSTATS)
    implementation("net.kyori", "adventure-serializer-configurate4")
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
            "minecraft" to Versions.MINECRAFT
        ))
    }
}

tasks["build"].dependsOn(tasks["shadowJar"])

jacoco {
    toolVersion = "0.8.7"
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
        create<MavenPublication>("kryptonServer") {
            artifactId = "krypton-server"

            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set("Krypton")
                description.set("Free and open-source Minecraft server software, written from the ground up.")
                url.set("https://www.kryptonmc.org")
                inceptionYear.set("2021")
                packaging = "jar"
                kryptonDetails()
            }
        }
    }
}

signing {
    sign(publishing.publications["kryptonServer"])
}

license {
    header(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine(false)
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
        xml.isEnabled = true
        html.isEnabled = true
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

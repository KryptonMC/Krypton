import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("krypton.common")
    id("info.solidsoft.pitest") version Versions.PITEST
    id("com.github.johnrengelman.shadow") version Versions.SHADOW
    `java-library`
}

evaluationDependsOn(":api")

repositories {
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.minestom.net/repository/maven-public/")
}

dependencies {
    implementation(project(":api"))
    implementation(project(":api").dependencyProject.sourceSets["ap"].output)
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
    implementation("org.kryptonmc", "datafixerupper", Versions.DFU) // Slight performance enhanced version, courtesy of Paper
    implementation("org.kryptonmc", "nbt", Versions.NBT)
    implementation("de.articdive", "articdata", Versions.MINECRAFT)

    // Miscellaneous
    implementation("com.github.ben-manes.caffeine", "caffeine", Versions.CAFFEINE)
    implementation("it.unimi.dsi", "fastutil", Versions.FASTUTIL)
    implementation("com.github.ajalt.clikt", "clikt", Versions.CLIKT)
    implementation("org.bstats", "bstats-base", Versions.BSTATS)
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
        val buildNumberProperty = System.getenv("BUILD_NUMBER")
        val buildNumber = if (buildNumberProperty != null) "-$buildNumberProperty" else ""
        archiveFileName.set("Krypton-${project.version}$buildNumber.jar")
        transform(Log4j2PluginsCacheFileTransformer::class.java)

        exclude("it/unimi/dsi/fastutil/booleans/**")
        exclude("it/unimi/dsi/fastutil/bytes/**")
        exclude("it/unimi/dsi/fastutil/chars/**")
        exclude("it/unimi/dsi/fastutil/floats/**")
        exclude("it/unimi/dsi/fastutil/io/**")
        exclude("it/unimi/dsi/fastutil/objects/*Reference*")
        exclude("it/unimi/dsi/fastutil/shorts/**")
    }
    withType<ProcessResources> {
        filter<ReplaceTokens>("tokens" to mapOf(
            "version" to project.version.toString(),
            "minecraft" to Versions.MINECRAFT
        ))
    }
}

tasks["build"].dependsOn(tasks["shadowJar"])

pitest {
    junit5PluginVersion.set("0.12")
    pitestVersion.set("1.6.7")
    avoidCallsTo.set(setOf(
        "kotlin.jvm.internal",
        "java.util.logging",
        "org.slf4j",
        "org.apache.logging.log4j"
    ))
    mutators.set(setOf(
        "CONDITIONALS_BOUNDARY",
        "INCREMENTS",
        "INVERT_NEGS",
        "MATH",
        "NEGATE_CONDITIONALS",
        "VOID_METHOD_CALLS",
        "EMPTY_RETURNS",
        "FALSE_RETURNS",
        "TRUE_RETURNS",
        "PRIMITIVE_RETURNS"
    ))
    targetClasses.set(setOf("org.kryptonmc.*"))
    targetTests.set(setOf("org.kryptonmc.*"))
    threads.set(Runtime.getRuntime().availableProcessors())
    outputFormats.set(setOf("HTML", "XML"))
    excludedMethods.set(setOf("equals", "hashCode", "toString", "emptyList"))
    excludedClasses.set(setOf(
        "org.kryptonmc.krypton.KryptonKt*",
        "org.kryptonmc.krypton.KryptonCLI*",
        "org.kryptonmc.krypton.KryptonServer*",
        "org.kryptonmc.krypton.NettyProcess*",
        "org.kryptonmc.krypton.WatchdogProcess*",
        "org.kryptonmc.krypton.auth.MojangUUIDSerializer*",
        "org.kryptonmc.krypton.auth.requests.SessionService*"
    ))
}

license {
    header.set(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine.set(false)
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
        "**/plugin/loader/KryptonPluginDescription.kt",
        "**/plugin/loader/PluginLoader.kt",
        "**/scheduling/KryptonScheduler.kt",
        "**/util/bytebufs.kt"
    )
}

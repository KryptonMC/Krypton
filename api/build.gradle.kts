import io.github.slimjar.task.SlimJar
import org.kryptonmc.krypton.Versions
import org.kryptonmc.krypton.adventure
import org.kryptonmc.krypton.applyCommon
import org.kryptonmc.krypton.applyRepositories
import org.kryptonmc.krypton.configurate
import org.kryptonmc.krypton.kotlinx
import org.kryptonmc.krypton.log4j
import org.kryptonmc.krypton.sponge

plugins {
    id("org.cadixdev.licenser")
    `maven-publish`
    signing
}

dependencies {
    // Kotlin
    slimApi(kotlin("stdlib"))

    // Core
    slimApi("com.google.guava:guava:${Versions.GUAVA}")
    slimApi("com.google.code.gson:gson:${Versions.GSON}")
    slimApi("org.apache.commons:commons-lang3:${Versions.COMMONS_LANG}")
    slimApi("org.apache.commons:commons-text:${Versions.COMMONS_TEXT}")
    slimApi(kotlinx("coroutines-core", Versions.COROUTINES))

    // Adventure
    slimApi(adventure("api"))
    slimApi(adventure("extra-kotlin"))
    slimApi(adventure("serializer-configurate4"))

    // Configurate
    slimApi(configurate("gson"))
    slimApi(configurate("hocon"))
    slimApi(configurate("extra-kotlin"))

    // Miscellaneous
    slimApi("com.mojang:brigadier:${Versions.BRIGADIER}")
    slimApi(sponge("math", Versions.MATH))
    slimApi(log4j("api"))
}

tasks.withType<SlimJar> {
    shade = false
}

pitest {
    excludedClasses.set(setOf("org.kryptonmc.api.effect.particle.*ParticleEffectBuilder"))
}

publishing {
    applyRepositories(project)
    publications {
        create<MavenPublication>("mavenKotlin") {
            applyCommon(project, "krypton-api")
            pom.applyCommon("Krypton API", "The official API for Krypton, the fast and lightweight Minecraft server written in Kotlin!")
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

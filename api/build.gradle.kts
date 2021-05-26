import org.kryptonmc.krypton.applyCommon
import org.kryptonmc.krypton.applyRepositories

plugins {
    id("org.cadixdev.licenser")
    `maven-publish`
    signing
}

pitest {
    excludedClasses.set(setOf("org.kryptonmc.krypton.api.effect.particle.*ParticleEffectBuilder"))
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

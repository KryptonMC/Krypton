import org.kryptonmc.krypton.applyCommon
import org.kryptonmc.krypton.applyRepositories

plugins {
    id("org.cadixdev.licenser")
    id("io.gitlab.arturbosch.detekt")
    `maven-publish`
    signing
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.17.1")
}

pitest {
    excludedClasses.set(setOf("org.kryptonmc.krypton.api.effect.particle.*ParticleEffectBuilder"))
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

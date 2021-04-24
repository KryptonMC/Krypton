import org.kryptonmc.krypton.applyCommon
import org.kryptonmc.krypton.applyRepositories

plugins {
    `maven-publish`
    signing
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

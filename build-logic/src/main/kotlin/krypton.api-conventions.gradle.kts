plugins {
    id("krypton.common-conventions")
    id("net.kyori.indra.publishing")
    id("org.jetbrains.dokka")
}

indra {
    publishReleasesTo("krypton", "https://repo.kryptonmc.org/releases")
    publishSnapshotsTo("krypton", "https://repo.kryptonmc.org/snapshots")
    includeJavaSoftwareComponentInPublications(false)
    configurePublications {
        artifactId = "krypton-${project.name}"
        from(components["kotlin"])
        pom {
            name.set("Krypton")
            description.set(rootProject.description)
            url.set("https://www.kryptonmc.org")
            inceptionYear.set("2021")
            packaging = "jar"

            organization {
                name.set("KryptonMC")
                url.set("https://github.com/KryptonMC")
            }

            developers {
                developer("bombardygamer", "Callum Seabrook", "callum.seabrook@prevarinite.com", "Europe/London", "Lead Developer")
                developer("therealjan", "Jan", "jan.m.tennert@gmail.com", "Europe/Berlin", "Developer")
            }

            contributors {
                contributor("Brandon Li", "brandonli2006ma@gmail.com", "America/New_York")
                contributor("Nicole Barningham", "esophose@gmail.com", "America/Boise")
                contributor("Alex Wood", "alexljwood24@hotmail.co.uk", "Europe/London")
            }

            distributionManagement {
                downloadUrl.set("https://ci.kryptonmc.org/job/Krypton/lastSuccessfulBuild/Krypton")
            }
        }
    }
}

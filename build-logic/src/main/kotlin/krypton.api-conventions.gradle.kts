plugins {
    id("krypton.common-conventions")
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

val sourceSets = extensions.getByName("sourceSets") as SourceSetContainer

task<Jar>("sourcesJar") {
    from(sourceSets.named("main").get().allSource)
    archiveClassifier.set("sources")
}

task<Jar>("javadocJar") {
    from(tasks["dokkaJavadoc"])
    archiveClassifier.set("javadoc")
}

publishing {
    repositories.maven {
        url = uri("https://repo.kryptonmc.org/releases")
        credentials(PasswordCredentials::class)
    }

    publications.create<MavenPublication>("mavenKotlin") {
        groupId = rootProject.group as String
        artifactId = "krypton-${project.name}"
        version = rootProject.version as String

        from(components["kotlin"])
        artifact(tasks["sourcesJar"])
        artifact(tasks["javadocJar"])

        pom {
            name.set("Krypton")
            description.set(rootProject.description)
            url.set("https://www.kryptonmc.org")
            inceptionYear.set("2021")
            packaging = "jar"

            developers {
                developer("bombardygamer", "Callum Seabrook", "callum.seabrook@prevarinite.com", "Europe/London", "Lead Developer")
                developer("therealjan", "Jan", "jan.m.tennert@gmail.com", "Europe/Berlin", "Developer")
            }

            contributors {
                contributor("Brandon Li", "brandonli2006ma@gmail.com", "America/New_York")
                contributor("Nicole Barningham", "esophose@gmail.com", "America/Boise")
                contributor("Alex Wood", "alexljwood24@hotmail.co.uk", "Europe/London")
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

signing {
    sign(publishing.publications["mavenKotlin"])
}

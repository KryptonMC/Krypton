plugins {
    id("krypton.common-conventions")
    `maven-publish`
}

tasks {
    javadoc {
        enabled = false
    }
    create<Jar>("allSourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }
}

publishing {
    repositories {
        maven {
            name = "krypton"
            url = uri("https://repo.kryptonmc.org/snapshots")

            if (System.getenv("PUBLISH_USERNAME") != null) {
                // If this env variable is present, we're running in CI, and we want to use the credentials from the secrets
                credentials {
                    username = System.getenv("PUBLISH_USERNAME")
                    password = System.getenv("PUBLISH_PASSWORD")
                }
            } else {
                // If the env variable isn't present, we're running locally, and we want to use the credentials from gradle.properties
                credentials(PasswordCredentials::class)
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = "krypton-${project.name}"
            from(components["kotlin"])
            artifact(tasks["allSourcesJar"])

            pom {
                name.set("Krypton")
                description.set(rootProject.description)
                url.set("https://www.kryptonmc.org")
                inceptionYear.set("2021")

                organization {
                    name.set("KryptonMC")
                    url.set("https://github.com/KryptonMC")
                }

                developers {
                    developer("bombardygamer", "Callum Seabrook", "callum.seabrook@prevarinite.com", "Europe/London", "Lead Developer")
                    developer("therealjan", "Jan", "jan.m.tennert@gmail.com", "Europe/Berlin", "Developer")
                    developer("tomlister", "Tom Lister", "tom@tomlister.net", "Australia/Sydney", "Developer")
                }

                contributors {
                    contributor("Nicole Barningham", "esophose@gmail.com", "America/Boise")
                    contributor("Alex Wood", "alexljwood24@hotmail.co.uk", "Europe/London")
                }

                scm {
                    connection.set("scm:git:https://github.com/KryptonMC/Krypton.git")
                    developerConnection.set("scm:git:ssh://git@github.com/KryptonMC/Krypton.git")
                    url.set("https://github.com/KryptonMC/Krypton")
                }

                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/KryptonMC/Krypton/issues")
                }

                ciManagement {
                    system.set("GitHub Actions")
                    url.set("https://github.com/KryptonMC/Krypton/actions")
                }

                distributionManagement {
                    downloadUrl.set("https://api.kryptonmc.org/downloads/v1/krypton/latest/download")
                }
            }
        }
    }
}

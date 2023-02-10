plugins {
    id("krypton.common-conventions")
    `maven-publish`
}

tasks {
    javadoc {
        enabled = false
    }
    javadocJar {
        from(dokkaJavadoc)
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        maven {
            name = "krypton"
            url = uri("https://repo.kryptonmc.org/releases")

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
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

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
}

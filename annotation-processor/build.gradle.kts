plugins {
    id("krypton.common")
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

dependencies {
    implementation(project(":api"))
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
        create<MavenPublication>("kryptonProcessor") {
            artifactId = "krypton-annotation-processor"

            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set("Krypton Annotation Processor")
                description.set("The annotation processor for the Krypton API")
                url.set("https://www.kryptonmc.org")
                inceptionYear.set("2021")
                packaging = "jar"
                kryptonDetails()
            }
        }
    }
}

signing {
    sign(publishing.publications["kryptonProcessor"])
}

license {
    header(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine(false)
}

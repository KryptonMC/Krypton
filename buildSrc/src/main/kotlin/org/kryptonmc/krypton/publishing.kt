/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomContributorSpec
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get

fun PublishingExtension.applyRepositories(project: Project) = repositories {
    maven {
        val releasesRepoURL = project.uri("https://repo.bristermitten.me/repository/maven-releases/")
        val snapshotsRepoURL = project.uri("https://repo.bristermitten.me/repository/maven-snapshots/")
        url = if (project.version.toString().endsWith("SNAPSHOT")) snapshotsRepoURL else releasesRepoURL

        credentials {
            username = project.property("maven.username", System.getenv("MAVEN_USERNAME"))
            password = project.property("maven.password", System.getenv("MAVEN_PASSWORD"))
        }
    }
}

fun MavenPublication.applyCommon(project: Project, artifact: String) {
    groupId = project.group.toString()
    artifactId = artifact
    version = project.version.toString()

    from(project.components["kotlin"])
    artifact(project.tasks["sourcesJar"])
    artifact(project.tasks["javadocJar"])
}

fun MavenPom.applyCommon(name: String, description: String) {
    this.name.set(name)
    this.description.set(description)
    url.set("https://www.kryptonmc.org")
    inceptionYear.set("2021")

    packaging = "jar"

    licenses {
        license {
            this.name.set("MIT License")
            this.url.set("https://opensource.org/licenses/MIT")
        }
    }

    developers {
        developer("bombardygamer", "Callum Seabrook", "callum.seabrook@prevarinite.com", "Europe/London", true)
        developer("knightzmc", "Alexander Wood", "alexwood2403@gmail.com", "Europe/London")
        developer("esophose", "Nicole Barningham", "esophose@gmail.com", "America/Boise")
    }

    contributors {
        contributor("Brandon Li", "brandonli2006ma@gmail.com", "America/New_York")
    }

    organization {
        this.name.set("KryptonMC")
        this.url.set("https://github.com/KryptonMC")
    }

    issueManagement {
        system.set("GitHub")
        this.url.set("https://github.com/KryptonMC/Krypton/issues")
    }

    ciManagement {
        system.set("Jenkins")
        this.url.set("https://ci.kryptonmc.org/job/Krypton")
    }

    distributionManagement {
        downloadUrl.set("https://ci.kryptonmc.org/job/Krypton/lastSuccessfulBuild/Krypton")
    }

    scm {
        connection.set("scm:git:git://github.com/KryptonMC/Krypton.git")
        developerConnection.set("scm:git:ssh://github.com:KryptonMC/Krypton.git")
        this.url.set("https://github.com/KryptonMC/Krypton")
    }
}

fun MavenPomDeveloperSpec.developer(id: String, name: String, email: String, timezone: String, isLead: Boolean = false) = developer {
    this.id.set(id)
    this.name.set(name)
    this.email.set(email)
    this.timezone.set(timezone)

    organization.set("KryptonMC")
    organizationUrl.set("https://github.com/KryptonMC")

    roles.set(setOf(if (isLead) "Lead Developer" else "Developer"))
}

fun MavenPomContributorSpec.contributor(name: String, email: String, timezone: String) = contributor {
    this.name.set(name)
    this.email.set(email)
    this.timezone.set(timezone)
    roles.set(setOf("Contributor"))
}

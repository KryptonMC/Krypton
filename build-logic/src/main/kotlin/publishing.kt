import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomContributorSpec
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import java.net.URI

fun RepositoryHandler.krypton(project: Project) {
    maven {
        val snapshots = URI("https://repo.kryptonmc.org/snapshots")
        val releases = URI("https://repo.kryptonmc.org/releases")
        url = if (project.version.toString().endsWith("SNAPSHOT")) snapshots else releases

        credentials {
            username = project.findProperty("maven.username")?.toString() ?: System.getenv("MAVEN_USERNAME")
            password = project.findProperty("maven.password")?.toString() ?: System.getenv("MAVEN_PASSWORD")
        }
    }
}

fun MavenPom.kryptonDetails() {
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

fun MavenPomDeveloperSpec.developer(id: String, name: String, email: String, timezone: String, vararg roles: String) = developer {
    this.id.set(id)
    this.name.set(name)
    this.email.set(email)
    this.timezone.set(timezone)
    organization.set("KryptonMC")
    organizationUrl.set("https://github.com/KryptonMC")
    this.roles.set(roles.toSet())
}

fun MavenPomContributorSpec.contributor(name: String, email: String, timezone: String) = contributor {
    this.name.set(name)
    this.email.set(email)
    this.timezone.set(timezone)
    roles.set(setOf("Contributor"))
}

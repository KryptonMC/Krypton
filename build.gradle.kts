plugins {
    id("krypton.base-conventions")
    id("org.ajoberstar.grgit") version "4.1.1"
}

allprojects {
    group = "org.kryptonmc"
    version = grgit.head().id.take(10)
    description = "Free and open-source Minecraft server software, written from the ground up."
}

val published = setOf(projects.annotationProcessor, projects.api, projects.server).map { it.dependencyProject }

subprojects {
    when (this) {
        in published -> plugins.apply("krypton.api-conventions")
        else -> plugins.apply("krypton.common-conventions")
    }
}

plugins {
    id("krypton.base-conventions")
}

allprojects {
    group = "org.kryptonmc"
    version = "0.69.1"
    description = "Free and open-source Minecraft server software, written from the ground up."
}

val published = setOf(projects.annotationProcessor, projects.api, projects.server).map { it.dependencyProject }

subprojects {
    when (this) {
        in published -> plugins.apply("krypton.api-conventions")
        else -> plugins.apply("krypton.common-conventions")
    }
}

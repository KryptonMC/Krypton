plugins {
    id("krypton.parent")
}

allprojects {
    group = "org.kryptonmc"
    version = "0.50.3"
    description = "Free and open-source Minecraft server software, written from the ground up."
}

val published = setOf(
    projects.annotationProcessor,
    projects.api,
    projects.server
).map { it.dependencyProject }

subprojects {
    when (this) {
        in published -> plugins.apply("krypton.api-conventions")
        else -> plugins.apply("krypton.common-conventions")
    }

    dependencies {
        "testImplementation"(rootProject.libs.bundles.junit)
        "testImplementation"(rootProject.libs.junit.platform.runner)
        "testImplementation"(rootProject.libs.mockk)
        "testRuntimeOnly"(rootProject.libs.bytebuddy)
    }
}

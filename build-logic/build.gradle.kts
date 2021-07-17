plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

object PluginVersions {

    const val LICENSER = "0.6.1"
    const val KOTLIN = "1.5.21"
    const val DOKKA = "1.4.30"
    const val PITEST = "1.6.0"
    const val DETEKT = "1.17.1"
}

dependencies {
    implementation("gradle.plugin.org.cadixdev.gradle", "licenser", PluginVersions.LICENSER)
    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", PluginVersions.KOTLIN)
    implementation("org.jetbrains.dokka", "dokka-gradle-plugin", PluginVersions.DOKKA)
    implementation("info.solidsoft.gradle.pitest", "gradle-pitest-plugin", PluginVersions.PITEST)
    implementation("io.gitlab.arturbosch.detekt", "detekt-gradle-plugin", PluginVersions.DETEKT)
}

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("gradle.plugin.org.cadixdev.gradle", "licenser", "0.6.1")
    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.6.0")
    implementation("org.jetbrains.dokka", "dokka-gradle-plugin", "1.4.30")
    implementation("org.spongepowered", "vanillagradle", "0.2")
    implementation("io.gitlab.arturbosch.detekt", "detekt-gradle-plugin", "1.19.0-RC1")
    implementation("gradle.plugin.com.github.johnrengelman", "shadow", "7.1.0")
}

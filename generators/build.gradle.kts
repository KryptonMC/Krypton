import org.spongepowered.gradle.vanilla.repository.MinecraftPlatform

plugins {
    id("krypton.common")
    id("org.spongepowered.gradle.vanilla") version "0.2"
}

dependencies {
    implementation("com.squareup", "kotlinpoet", "1.10.1")
    implementation("org.apache.logging.log4j", "log4j-core", "2.14.0")
}

minecraft {
    version("1.17.1")
    platform(MinecraftPlatform.SERVER)
}

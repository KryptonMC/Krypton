import org.spongepowered.gradle.vanilla.repository.MinecraftPlatform

plugins {
    id("krypton.common-conventions")
    id("org.spongepowered.gradle.vanilla")
}

dependencies {
    implementation(libs.kotlinpoet)
    implementation(libs.log4j.core)
}

minecraft {
    version(global.versions.minecraft.get())
    platform(MinecraftPlatform.SERVER)
}

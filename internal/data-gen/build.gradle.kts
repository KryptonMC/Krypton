plugins {
    id("krypton.basic-conventions")
    id("org.spongepowered.gradle.vanilla")
}

dependencies {
    implementation(libs.gson)
}

minecraft {
    version(global.versions.minecraft.get())
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}

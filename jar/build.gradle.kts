import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    id("org.kryptonmc.downloads.upload")
}

dependencies {
    implementation(projects.server)
    implementation(projects.bansPlugin)
    implementation(projects.whitelistPlugin)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    withType<ShadowJar> {
        archiveFileName.set("Krypton.jar")
        transform(Log4j2PluginsCacheFileTransformer::class.java)

        fastutilExclusions("booleans", "bytes", "chars", "floats", "io", "shorts")
        dataExclusions(global.versions.minecraft.get(), "attributes", "biomes", "block_properties", "commands", "custom_statistic",
            "dimension_types", "dye_colors", "enchantments", "entities", "entity_data_serializers", "fluid_properties", "fluids", "game_events",
            "map_colors", "packets", "particles", "potion_effects", "potions", "recipes", "sound_sources", "villager_professions",
            "villager_types")
        exclude("1_19_3_loot_tables/**")
        exclude("CONTENT_DOCUMENTATION.md", "TOC.md")
        exclude(".idea/**") // Not sure why but some stuff from the .idea folder seems to appear in the JAR
        exclude("dataconverter.mixins.json", "fabric.mod.json") // Stuff left in from DataConverter that doesn't make any sense for us.

        relocate("org.bstats", "org.kryptonmc.krypton.bstats")
    }
    uploadToApi {
        dependsOn(shadowJar)
        file.set(shadowJar.get().archiveFile)
    }
}

configureJarMetadata("org.kryptonmc.server") {
    put("Implementation-Title", "Krypton")
    put("Implementation-Vendor", "KryptonMC")
    put("Implementation-Version", version.toString())
    put("Main-Class", "org.kryptonmc.krypton.KryptonKt")
    put("Multi-Release", "true")
}

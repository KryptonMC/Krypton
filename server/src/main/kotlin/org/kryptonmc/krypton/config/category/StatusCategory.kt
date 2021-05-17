package org.kryptonmc.krypton.config.category

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class StatusCategory(
    @Comment("The message of the day. Supports legacy and hex codes (using &#).")
    val motd: TextComponent = Component.text("Krypton is a Minecraft server written in Kotlin!", TextColor.color(128, 0, 255)),
    @Setting("max-players")
    @Comment("The upper limit of the player count. Any players that try to join when this is reached will be kicked.")
    val maxPlayers: Int = 20
)

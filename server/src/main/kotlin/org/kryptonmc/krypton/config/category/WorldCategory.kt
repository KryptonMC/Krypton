package org.kryptonmc.krypton.config.category

import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.Gamemode
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class WorldCategory(
    @Comment("The name of the folder with the world to load in it.")
    val name: String = "world",
    @Comment("The gamemode for this world. Valid values are: 0-3 (legacy), survival, creative, adventure and spectator (case insensitive).")
    val gamemode: Gamemode = Gamemode.SURVIVAL,
    @Setting("force-default-gamemode")
    @Comment("Forces the above gamemode for all players in all worlds.")
    val forceDefaultGamemode: Boolean = false,
    @Comment("The default difficulty. Valid values are: 0-3 (legacy), peaceful, easy, normal and hard (case insensitive).")
    val difficulty: Difficulty = Difficulty.NORMAL,
    @Comment("If this server is in hardcore mode. Currently does nothing.")
    val hardcore: Boolean = false,
    @Setting("view-distance")
    @Comment("The render distance of the server. This is how many chunks you can see in front of you, excluding the one you are in.")
    val viewDistance: Int = 10,
    @Setting("autosave-interval")
    @Comment("The amount of time (in ticks) between automatic world saves.")
    val autosaveInterval: Int = 6000
)

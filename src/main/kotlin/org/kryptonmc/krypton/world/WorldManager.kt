package org.kryptonmc.krypton.world

import net.kyori.adventure.nbt.BinaryTagIO
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.space.Position
import java.io.File
import java.net.URI
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Suppress("MemberVisibilityCanBePrivate")
class WorldManager {

    val worlds = mutableListOf<World>()

    init {
        LOGGER.debug("Loading world hardcore...")
        val folder = File(URI.create("file://${System.getProperty("krypton.world.dir")}"))
        worlds += loadWorld(File(folder, "level.dat"))
        LOGGER.debug("World loaded!")
    }

    fun loadWorld(file: File): World {
        val nbt = BinaryTagIO.unlimitedReader().read(file.toPath(), BinaryTagIO.Compression.GZIP).getCompound("Data")

//        val bossbars = nbt.getCompound("CustomBossEvents").map { bossbar ->
//            val nbtBossbar = bossbar as CompoundBinaryTag
//            val players = nbtBossbar.getList("Players").map {
//                val nbtPlayerUUID = it as CompoundBinaryTag
//                UUID(nbtPlayerUUID.getLong("M"), nbtPlayerUUID.getLong("L"))
//            }
//
//            Bossbar(
//                bossbar.key.toNamespacedKey(),
//                Json.Default.decodeFromString(nbtBossbar.getString("Name")),
//                LegacyColor.fromLegacy(nbtBossbar.getString("Color")),
//                BossbarOverlay.valueOf(nbtBossbar.getString("Overlay")),
//                nbtBossbar.getInt("Max"),
//                nbtBossbar.getInt("Value"),
//                nbtBossbar.getBoolean("CreateWorldFog"),
//                nbtBossbar.getBoolean("DarkenScreen"),
//                nbtBossbar.getBoolean("PlayBossMusic"),
//                nbtBossbar.getBoolean("Visible"),
//                players
//            )
//        }

        val worldBorder = WorldBorder(
            nbt.getDouble("BorderSize"),
            nbt.getDouble("BorderCenterX"),
            nbt.getDouble("BorderCenterZ"),
            nbt.getDouble("BorderDamagePerBlock"),
            nbt.getDouble("BorderSafeZone"),
            nbt.getDouble("BorderSizeLerpTarget"),
            nbt.getLong("BorderSizeLerpTime"),
            nbt.getDouble("BorderWarningBlocks"),
            nbt.getDouble("BorderWarningTime")
        )

//        val endDimensionData = nbt.getCompound("DimensionData")
//            .getCompound("1")
//            .getCompound("DragonFight")
//            .let { dragonFight ->
//                val exitPortal = dragonFight.getCompound("ExitPortalLocation").let {
//                    BlockPosition(it.getByte("X").toInt(), it.getByte("Y").toInt(), it.getByte("Z").toInt())
//                }
//                val gateways = dragonFight.getList("Gateways").map { (it as IntBinaryTag).value() }
//                EndDimensionData(
//                    exitPortal,
//                    gateways,
//                    dragonFight.getBoolean("DragonKilled"),
//                    UUID(dragonFight.getLong("DragonUUIDMost"), dragonFight.getLong("DragonUUIDLeast")),
//                    dragonFight.getBoolean("PreviouslyKilled")
//                )
//            }

//        val gamerules = nbt.getCompound("GameRules").map { (key, value) ->
//            Gamerule(GameruleType.valueOf(key), value)
//        }

//        val worldGenSettings = nbt.getCompound("WorldGenSettings").let { settings ->
//            val dimensions = settings.getCompound("dimensions").map { dimension ->
//                (dimension as CompoundBinaryTag).let {
//                    Dimension(
//                        it.getString("type").toNamespacedKey(),
//                        Generator.fromNBT(it)
//                    )
//                }
//            }
//
//            WorldGenerationSettings(
//                settings.getLong("seed"),
//                settings.getBoolean("generate_features"),
//            )
//        }

        val spawnLocation = Position(
            nbt.getInt("SpawnX"),
            nbt.getInt("SpawnY"),
            nbt.getInt("SpawnZ")
        )

        return World(
            nbt.getString("LevelName"),
            worldBorder,
            nbt.getLong("DayTime"),
            Difficulty.fromId(nbt.getByte("Difficulty").toInt()),
            nbt.getBoolean("DifficultyLocked"),
            LocalDateTime.ofInstant(Instant.ofEpochMilli(nbt.getLong("LastPlayed")), ZoneOffset.UTC),
            spawnLocation,
            nbt.getLong("Time")
        )
    }

    companion object {

        private val LOGGER = logger<WorldManager>()
    }
}
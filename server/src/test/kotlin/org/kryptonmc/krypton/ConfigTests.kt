/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.SerializationException
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.server.DifficultySerializer
import org.kryptonmc.krypton.server.GamemodeSerializer
import org.kryptonmc.krypton.server.KryptonConfig
import org.kryptonmc.krypton.server.ServerConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConfigTests {

    @Test
    fun `test config loads properly with correct values`() {
        val parsed = ConfigFactory.parseResources("config.conf")
        val config = Hocon.decodeFromConfig<KryptonConfig>(parsed)

        // Server settings
        assertEquals("0.0.0.0", config.server.ip)
        assertEquals(25565, config.server.port)
        assertTrue(config.server.onlineMode)
        assertEquals(256, config.server.compressionThreshold)

        // Status settings
        assertEquals(Component.text("Krypton is a Minecraft server written in Kotlin!", TextColor.fromHexString("#8000ff")), config.status.motd)
        assertEquals(20, config.status.maxPlayers)

        // World settings
        assertEquals("world", config.world.name)
        assertEquals(Gamemode.SURVIVAL, config.world.gamemode)
        assertFalse(config.world.forceDefaultGamemode)
        assertEquals(Difficulty.NORMAL, config.world.difficulty)
        assertFalse(config.world.hardcore)
        assertEquals(10, config.world.viewDistance)
        assertEquals(6000, config.world.autosaveInterval)

        // Advanced settings
        assertTrue(config.advanced.synchronizeChunkWrites)
        assertTrue(config.advanced.enableJmxMonitoring)

        // Query settings
        assertFalse(config.query.enabled)
        assertEquals(25566, config.query.port)

        // Other settings
        assertFalse(config.other.bungeecord)
        assertTrue(config.other.metrics)
        assertEquals(60, config.other.timeoutTime)
        assertTrue(config.other.restartOnCrash)
        assertEquals("./start.sh", config.other.restartScript)
        assertEquals(5000, config.other.earlyWarningInterval)
        assertEquals(10000, config.other.earlyWarningDelay)
        assertEquals(5, config.other.saveThreshold)

        val modified = config.copy(
            server = config.server.copy(onlineMode = false),
            world = config.world.copy(forceDefaultGamemode = true, hardcore = true),
            advanced = config.advanced.copy(synchronizeChunkWrites = false, enableJmxMonitoring = false),
            query = config.query.copy(enabled = true),
            other = config.other.copy(bungeecord = true, metrics = false, restartOnCrash = false)
        )
        assertFalse(modified.server.onlineMode)
        assertTrue(modified.world.forceDefaultGamemode)
        assertTrue(modified.world.hardcore)
        assertFalse(modified.advanced.synchronizeChunkWrites)
        assertFalse(modified.advanced.enableJmxMonitoring)
        assertTrue(modified.query.enabled)
        assertTrue(modified.other.bungeecord)
        assertFalse(modified.other.metrics)
        assertFalse(modified.other.restartOnCrash)
    }

    @Test
    fun `test config defaults`() {
        val config = KryptonConfig()

        // Server settings
        assertEquals("0.0.0.0", config.server.ip)
        assertEquals(25565, config.server.port)
        assertTrue(config.server.onlineMode)
        assertEquals(256, config.server.compressionThreshold)

        // Status settings
        assertEquals(Component.text("Krypton is a Minecraft server written in Kotlin!", TextColor.fromHexString("#8000ff")), config.status.motd)
        assertEquals(20, config.status.maxPlayers)

        // World settings
        assertEquals("world", config.world.name)
        assertEquals(Gamemode.SURVIVAL, config.world.gamemode)
        assertFalse(config.world.forceDefaultGamemode)
        assertEquals(Difficulty.NORMAL, config.world.difficulty)
        assertFalse(config.world.hardcore)
        assertEquals(10, config.world.viewDistance)
        assertEquals(6000, config.world.autosaveInterval)

        // Advanced settings
        assertTrue(config.advanced.synchronizeChunkWrites)
        assertTrue(config.advanced.enableJmxMonitoring)

        // Query settings
        assertFalse(config.query.enabled)
        assertEquals(25566, config.query.port)

        // Other settings
        assertFalse(config.other.bungeecord)
        assertTrue(config.other.metrics)
        assertEquals(60, config.other.timeoutTime)
        assertTrue(config.other.restartOnCrash)
        assertEquals("./start.sh", config.other.restartScript)
        assertEquals(5000, config.other.earlyWarningInterval)
        assertEquals(10000, config.other.earlyWarningDelay)
        assertEquals(5, config.other.saveThreshold)
    }

    @Test
    fun `test gamemode deserialization`() {
        val gamemodeString = "\"adventure\""
        val gamemodeId = "\"2\""
        assertEquals(Gamemode.ADVENTURE, Json.decodeFromString(GamemodeSerializer, gamemodeString))
        assertEquals(Gamemode.ADVENTURE, Json.decodeFromString(GamemodeSerializer, gamemodeId))

        assertThrows<IllegalArgumentException> { Json.decodeFromString(GamemodeSerializer, "\"4\"") }
        assertThrows<IllegalArgumentException> { Json.decodeFromString(GamemodeSerializer, "\"-1\"") }
    }

    @Test
    fun `test difficulty deserialization`() {
        val difficultyString = "\"normal\""
        val difficultyId = "\"2\""
        assertEquals(Difficulty.NORMAL, Json.decodeFromString(DifficultySerializer, difficultyString))
        assertEquals(Difficulty.NORMAL, Json.decodeFromString(DifficultySerializer, difficultyId))

        assertThrows<IllegalArgumentException> { Json.decodeFromString(GamemodeSerializer, "\"4\"") }
        assertThrows<IllegalArgumentException> { Json.decodeFromString(GamemodeSerializer, "\"-1\"") }
    }
}

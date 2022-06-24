/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world

import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

abstract class CommandBlockHandler(override val server: KryptonServer) : Sender {

    var command: String = ""
    private var lastExecution = -1L
    private var updateLastExecution = true
    private var successCount = 0
    private var trackOutput = true
    var lastOutput: Component? = null

    override var name: Component = DEFAULT_NAME
    override val uuid: UUID = Identity.nil().uuid()

    protected abstract fun onUpdated()

    fun load(data: CompoundTag) {
        command = data.getString("Command")
        successCount = data.getInt("SuccessCount")
        if (data.contains("CustomName", StringTag.ID)) name = GsonComponentSerializer.gson().deserialize(data.getString("CustomName"))
        if (data.contains("TrackOutput", ByteTag.ID)) trackOutput = data.getBoolean("TrackOutput")
        lastOutput = if (data.contains("LastOutput", StringTag.ID)) {
            try {
                GsonComponentSerializer.gson().deserialize(data.getString("LastOutput"))
            } catch (exception: Exception) {
                Component.text(exception.message ?: "Error occurred while deserializing last output")
            }
        } else {
            null
        }
        if (data.contains("UpdateLastExecution")) updateLastExecution = data.getBoolean("UpdateLastExecution")
        lastExecution = if (updateLastExecution && data.contains("LastExecution")) data.getLong("LastExecution") else -1L
    }

    fun save(builder: CompoundTag.Builder): CompoundTag.Builder = builder.apply {
        string("Command", command)
        int("SuccessCount", successCount)
        string("CustomName", GsonComponentSerializer.gson().serialize(name))
        boolean("TrackOutput", trackOutput)
        if (lastOutput != null && trackOutput) string("LastOutput", GsonComponentSerializer.gson().serialize(lastOutput!!))
        boolean("UpdateLastExecution", updateLastExecution)
        if (updateLastExecution && lastExecution > 0L) long("LastExecution", lastExecution)
    }

    fun runCommand(world: KryptonWorld): Boolean {
        if (world.time == lastExecution) return false
        if (command == "Searge") {
            lastOutput = Component.text("#itzlipofutzli")
            successCount = 1
            return true
        }
        successCount = 0
        if (server.config.world.allowCommandBlocks && command.isNotEmpty()) {
            lastOutput = null
            server.commandManager.dispatch(this, command) { _, result, _ -> if (result) successCount++ }
        }
        lastExecution = if (updateLastExecution) world.time else -1L
        return true
    }

    override fun sendMessage(message: Component) {
        if (!trackOutput) return
        lastOutput = Component.text().content("[${TIME_FORMATTER.format(LocalDateTime.now())}] ").append(message).build()
        onUpdated()
    }

    override fun getPermissionValue(permission: String): TriState = TriState.TRUE

    override fun identity(): Identity = Identity.nil()

    companion object {

        private val DEFAULT_NAME = Component.text("@")
        private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss")
    }
}

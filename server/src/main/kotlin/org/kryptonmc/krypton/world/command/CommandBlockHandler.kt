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
package org.kryptonmc.krypton.world.command

import com.mojang.brigadier.ResultConsumer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.util.TriState
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.rule.GameRuleKeys
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class CommandBlockHandler : KryptonSender {

    var command: String = ""
        set(value) {
            field = value
            successCount = 0
        }
    private var lastExecution = -1L
    private var updateLastExecution = true
    private var successCount = 0
    private var trackOutput = true
    var lastOutput: Component? = null
    private val commandCallback = ResultConsumer<CommandSourceStack> { _, success, _ -> if (success) successCount++ }

    override var name: Component = DEFAULT_NAME
    override val server: KryptonServer
        get() = world().server

    protected abstract fun world(): KryptonWorld

    abstract override fun createCommandSourceStack(): CommandSourceStack

    protected abstract fun onUpdated()

    fun load(data: CompoundTag) {
        command = data.getString(COMMAND_TAG)
        successCount = data.getInt(SUCCESS_COUNT_TAG)
        if (data.contains(CUSTOM_NAME_TAG, StringTag.ID)) name = GsonComponentSerializer.gson().deserialize(data.getString(CUSTOM_NAME_TAG))
        if (data.contains(TRACK_OUTPUT_TAG, ByteTag.ID)) trackOutput = data.getBoolean(TRACK_OUTPUT_TAG)
        lastOutput = if (data.contains(LAST_OUTPUT_TAG, StringTag.ID)) {
            try {
                GsonComponentSerializer.gson().deserialize(data.getString(LAST_OUTPUT_TAG))
            } catch (exception: Exception) {
                Component.text(exception.message ?: "Error occurred while deserializing last output")
            }
        } else {
            null
        }
        if (data.contains(UPDATE_LAST_EXECUTION_TAG)) updateLastExecution = data.getBoolean(UPDATE_LAST_EXECUTION_TAG)
        lastExecution = if (updateLastExecution && data.contains(LAST_EXECUTION_TAG)) data.getLong(LAST_EXECUTION_TAG) else -1L
    }

    fun save(builder: CompoundTag.Builder): CompoundTag.Builder = builder.apply {
        putString(COMMAND_TAG, command)
        putInt(SUCCESS_COUNT_TAG, successCount)
        putString(CUSTOM_NAME_TAG, GsonComponentSerializer.gson().serialize(name))
        putBoolean(TRACK_OUTPUT_TAG, trackOutput)
        if (lastOutput != null && trackOutput) putString(LAST_OUTPUT_TAG, GsonComponentSerializer.gson().serialize(lastOutput!!))
        putBoolean(UPDATE_LAST_EXECUTION_TAG, updateLastExecution)
        if (updateLastExecution && lastExecution > 0L) putLong(LAST_EXECUTION_TAG, lastExecution)
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
            server.commandManager.dispatch(createCommandSourceStack(), command, commandCallback)
        }
        lastExecution = if (updateLastExecution) world.time else -1L
        return true
    }

    override fun sendSystemMessage(message: Component) {
        if (trackOutput) {
            lastOutput = Component.text().content("[${TIME_FORMATTER.format(LocalDateTime.now())}] ").append(message).build()
            onUpdated()
        }
    }

    override fun getPermissionValue(permission: String): TriState = TriState.TRUE

    override fun acceptsSuccess(): Boolean = world().gameRules().getBoolean(GameRuleKeys.SEND_COMMAND_FEEDBACK) && trackOutput

    override fun acceptsFailure(): Boolean = trackOutput

    override fun shouldInformAdmins(): Boolean = world().gameRules().getBoolean(GameRuleKeys.COMMAND_BLOCK_OUTPUT)

    companion object {

        private val DEFAULT_NAME = Component.text("@")
        private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss")

        private const val COMMAND_TAG = "Command"
        private const val SUCCESS_COUNT_TAG = "SuccessCount"
        private const val CUSTOM_NAME_TAG = "CustomName"
        private const val TRACK_OUTPUT_TAG = "TrackOutput"
        private const val LAST_OUTPUT_TAG = "LastOutput"
        private const val UPDATE_LAST_EXECUTION_TAG = "UpdateLastExecution"
        private const val LAST_EXECUTION_TAG = "LastExecution"
    }
}

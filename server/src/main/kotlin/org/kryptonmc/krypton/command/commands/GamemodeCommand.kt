/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.command.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.PlayerAction.UPDATE_GAMEMODE
import org.kryptonmc.krypton.util.argument

class GamemodeCommand(private val server: KryptonServer) : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val node = literal<Sender>("gamemode")
        Gamemode.values().forEach { mode ->
            node.then(argument<Sender, String>(mode.name.lowercase(), StringArgumentType.string())
                .executes {
                    updateGameMode(it.source as? KryptonPlayer ?: return@executes 1, Gamemode.valueOf(it.argument<String>(mode.name.lowercase()).uppercase()))
                    1
                })
        }
        dispatcher.register(node)
    }

    private fun updateGameMode(player: KryptonPlayer, mode: Gamemode) {
        player.gamemode = mode
        server.playerManager.sendToAll(PacketOutPlayerInfo(UPDATE_GAMEMODE, player))
        player.sendMessage(translatable("commands.gamemode.success.self", listOf(translatable("gameMode.${mode.name.lowercase()}"))))
    }
}

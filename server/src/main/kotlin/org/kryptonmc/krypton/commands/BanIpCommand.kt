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
package org.kryptonmc.krypton.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.server.ban.BanEntry
import org.kryptonmc.krypton.server.ban.BannedIpEntry
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.util.asString

object BanIpCommand : InternalCommand {

    private val ALREADY_BANNED = SimpleCommandExceptionType(translatable("commands.banip.failed").toMessage())
    val IP_ADDRESS_PATTERN = Regex("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("ban-ip")
            .permission("krypton.command.banip", 3)
            .then(argument<Sender, String>("target", string())
                .executes {
                    val server = it.source.server as? KryptonServer ?: return@executes 0
                    banIp(server, it.argument("target"), it.source)
                    1
                }.then(argument<Sender, String>("reason", string())
                    .executes {
                        val server = it.source.server as? KryptonServer ?: return@executes 0
                        banIp(server, it.argument("target"), it.source, it.argument("reason"))
                        1
                    })
            )
        )
    }

    private fun banIp(server: KryptonServer, target: String, sender: Sender, reason: String = "Banned by operator") {
        if (target.matches(IP_ADDRESS_PATTERN)) { // The target is an IP address
            // Check player is not already banned
            if (server.playerManager.bannedIps.contains(target)) throw ALREADY_BANNED.create()
            val entry = BannedIpEntry(target, reason = reason)
            server.playerManager.bannedIps.add(entry)

            // Send success
            sender.sendMessage(translatable("commands.banip.success", text(target), text(reason)))
            logBan(target, sender.name, reason, server)
        } else if (server.player(target) != null) { // The target is a player
            val player = server.player(target)!!
            val entry = BannedIpEntry(player.address.asString(), reason = reason)

            // Check player is not already banned
            if (server.playerManager.bannedIps.contains(entry.key)) throw ALREADY_BANNED.create()
            server.playerManager.bannedIps.add(entry)

            // Construct banned message and disconnect target
            val text = translatable("multiplayer.disconnect.banned_ip.reason", text(entry.reason))
            if (entry.expiryDate != null) text.append(translatable(
                "multiplayer.disconnect.banned.expiration",
                text(BanEntry.DATE_FORMATTER.format(entry.expiryDate))
            ))
            player.disconnect(text)

            // Send success
            sender.sendMessage(translatable("commands.banip.success", text(target), text(entry.reason)))
            logBan(target, sender.name, reason, server)
        } else { // The target isn't an IP address or a player
            sender.sendMessage(translatable("commands.banip.invalid"))
        }
    }

    private fun logBan(
        target: String,
        source: String,
        reason: String,
        server: KryptonServer
    ) = server.console.sendMessage(translatable("commands.banlist.entry", text(target), text(source), text(reason)))
}

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
package org.kryptonmc.krypton.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.server.ban.BanEntry
import org.kryptonmc.krypton.server.ban.BannedIpEntry
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.util.asString

object BanIpCommand : InternalCommand {

    private val ALREADY_BANNED = SimpleCommandExceptionType(Component.translatable("commands.banip.failed").toMessage())

    @JvmField
    val IP_ADDRESS_PATTERN: Regex = Regex("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("ban-ip") {
            permission(KryptonPermission.BAN_IP)
            argument("target", StringArgumentType.string()) {
                runs {
                    val server = it.source.server as? KryptonServer ?: return@runs
                    banIp(server, it.argument("target"), it.source, "Banned by operator")
                    Command.SINGLE_SUCCESS
                }
                argument("reason", StringArgumentType.string()) {
                    runs {
                        val server = it.source.server as? KryptonServer ?: return@runs
                        banIp(server, it.argument("target"), it.source, it.argument("reason"))
                        Command.SINGLE_SUCCESS
                    }
                }
            }
        })
    }

    @JvmStatic
    private fun banIp(server: KryptonServer, target: String, sender: Sender, reason: String) {
        if (target.matches(IP_ADDRESS_PATTERN)) {
            // The target is an IP address
            // Check player is not already banned
            if (server.playerManager.bannedIps.contains(target)) throw ALREADY_BANNED.create()
            val entry = BannedIpEntry(target, reason = LegacyComponentSerializer.legacySection().deserialize(reason))
            server.playerManager.bannedIps.add(entry)

            // Send success
            sender.sendMessage(Component.translatable("commands.banip.success", Component.text(target), Component.text(reason)))
            return
        }
        val player = server.player(target)
        if (player != null) {
            // The target is a player
            val entry = BannedIpEntry(player.address.asString(), reason = LegacyComponentSerializer.legacySection().deserialize(reason))

            // Check player is not already banned
            if (server.playerManager.bannedIps.contains(entry.key)) throw ALREADY_BANNED.create()
            server.playerManager.bannedIps.add(entry)

            // Construct banned message and disconnect target
            val text = Component.translatable("multiplayer.disconnect.banned_ip.reason", entry.reason)
            if (entry.expirationDate != null) {
                val expirationDate = Component.text(BanEntry.DATE_FORMATTER.format(entry.expirationDate))
                text.append(Component.translatable("multiplayer.disconnect.banned.expiration", expirationDate))
            }
            player.disconnect(text)

            // Send success
            sender.sendMessage(Component.translatable("commands.banip.success", Component.text(target), entry.reason))
            return
        }
        // The target isn't an IP address or a player
        sender.sendMessage(Component.translatable("commands.banip.invalid"))
    }
}

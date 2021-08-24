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
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.server.ban.BanEntry
import org.kryptonmc.krypton.server.ban.BannedIpEntry
import org.kryptonmc.krypton.util.argument
import org.kryptonmc.krypton.util.stringify
import org.kryptonmc.krypton.util.toComponent

object BanIpCommand : InternalCommand {

    private val ALREADY_BANNED_EXCEPTION = SimpleCommandExceptionType(translatable("commands.banip.failed").toMessage())
    val PATTERN = Regex("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("ban-ip")
            .permission("krypton.command.banip", 3)
            .then(argument<Sender, String>("target", string())
                .executes {
                    banIp(it.source.server as? KryptonServer ?: return@executes 0, it.argument("target"), it.source)
                    1
                }.then(argument<Sender, String>("reason", string())
                    .executes {
                        banIp(it.source.server as? KryptonServer ?: return@executes 0, it.argument("target"), it.source, it.argument("reason"))
                        1
                    })
            )
        )
    }

    private fun banIp(server: KryptonServer, target: String, sender: Sender, reason: String = "Banned by operator") {
        if (target.matches(PATTERN)) {
            if (server.playerManager.bannedIps.contains(target)) throw ALREADY_BANNED_EXCEPTION.create()
            val entry = BannedIpEntry(target, reason = reason)
            server.playerManager.bannedIps += entry
            sender.sendMessage(translatable("commands.banip.success", target.toComponent(), reason.toComponent()))
            logBan(target, sender.name, reason, server)
        } else if (server.player(target) != null) {
            val player = server.player(target)!!
            val entry = BannedIpEntry(player.address.stringify(), reason = reason)
            if (server.playerManager.bannedIps.contains(entry.key)) throw ALREADY_BANNED_EXCEPTION.create()
            server.playerManager.bannedIps += entry
            val text = translatable("multiplayer.disconnect.banned_ip.reason", entry.reason.toComponent())
            if (entry.expiryDate != null) text.append(translatable("multiplayer.disconnect.banned.expiration", BanEntry.DATE_FORMATTER.format(entry.expiryDate).toComponent()))
            player.disconnect(text)
            sender.sendMessage(translatable("commands.banip.success", target.toComponent(), entry.reason.toComponent()))
            logBan(target, sender.name, reason, server)
        } else sender.sendMessage(translatable("commands.banip.invalid"))
    }

    private fun logBan(
        target: String,
        source: String,
        reason: String,
        server: KryptonServer
    ) = server.console.sendMessage(translatable("commands.banlist.entry", target.toComponent(), source.toComponent(), reason.toComponent()))
}

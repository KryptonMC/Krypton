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

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.locale.CommandMessages
import org.kryptonmc.krypton.server.ban.BanManager
import org.kryptonmc.krypton.server.ban.KryptonIpBan
import org.kryptonmc.krypton.util.AddressUtil

object BanIpCommand {

    private val ERROR_INVALID_IP = CommandExceptions.simple("commands.banip.invalid")
    private val ERROR_ALREADY_BANNED = CommandExceptions.simple("commands.banip.failed")

    private const val TARGET = "target"
    private const val REASON = "reason"
    private const val DEFAULT_REASON = "Banned by operator."

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("ban-ip") {
            requiresPermission(KryptonPermission.BAN_IP)
            argument(TARGET, StringArgumentType.string()) {
                runs { banIp(it.source, it.getArgument(TARGET), DEFAULT_REASON) }
                argument(REASON, StringArgumentType.string()) {
                    runs { banIp(it.source, it.getArgument(TARGET), it.getArgument(REASON)) }
                }
            }
        })
    }

    @JvmStatic
    private fun banIp(source: CommandSourceStack, target: String, reason: String) {
        val banManager = source.server.playerManager.banManager
        if (BanCommandUtil.IP_ADDRESS_PATTERN.matcher(target).matches()) {
            // The target is an IP address
            doBan(source, banManager, target, reason)
            return
        }
        val player = source.server.playerManager.getPlayer(target)
        if (player != null) {
            // The target is a player
            val ban = doBan(source, banManager, AddressUtil.asString(player.address), reason)
            player.disconnect(BanCommandUtil.createBanMessage("banned_ip", ban.reason, ban.expirationDate))
            return
        }
        // The target isn't an IP address or a player
        throw ERROR_INVALID_IP.create()
    }

    @JvmStatic
    private fun doBan(source: CommandSourceStack, manager: BanManager, ip: String, reason: String): KryptonIpBan {
        if (manager.isBanned(ip)) throw ERROR_ALREADY_BANNED.create()
        val ban = KryptonIpBan(ip, reason = LegacyComponentSerializer.legacySection().deserialize(reason))
        manager.addBan(ban)
        CommandMessages.BAN_IP.sendSuccess(source, ip, ban.reason, true)
        return ban
    }
}

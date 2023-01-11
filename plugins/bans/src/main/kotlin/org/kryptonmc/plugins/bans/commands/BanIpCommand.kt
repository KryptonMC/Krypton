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
package org.kryptonmc.plugins.bans.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.kyori.adventure.text.Component
import org.kryptonmc.api.Server
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.adventure.Components
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.plugins.bans.api.IpBan
import org.kryptonmc.plugins.bans.storage.KryptonBanManager
import org.kryptonmc.plugins.bans.util.AddressConverter

class BanIpCommand(private val banManager: KryptonBanManager) {

    fun create(): LiteralArgumentBuilder<CommandExecutionContext> = literalCommand("ban-ip") {
        requires { it.sender.hasPermission("krypton.command.banip") }
        argument(TARGET, StringArgumentType.word()) {
            executes { banIpOrName(it.source, StringArgumentType.getString(it, TARGET), null) }
            argument(REASON, StringArgumentType.greedyString()) {
                executes { banIpOrName(it.source, StringArgumentType.getString(it, TARGET), StringArgumentType.getString(it, REASON)) }
            }
        }
    }

    private fun banIpOrName(source: CommandExecutionContext, text: String, reason: String?): Int {
        if (BanCommandUtil.IP_ADDRESS_PATTERN.matcher(text).matches()) return banIp(source, text, reason)
        // Wasn't an IP, let's try and find a player and get their IP
        val player = source.server.getPlayer(text)
        if (player != null) return banIp(source, AddressConverter.asString(player.address), reason)
        // Could not find a player or IP that matches the input, throw an error
        throw ERROR_INVALID_IP.create()
    }

    private fun banIp(source: CommandExecutionContext, ip: String, reason: String?): Int {
        if (banManager.isBanned(ip)) throw ERROR_ALREADY_BANNED.create()
        val ban = IpBan(ip, source.textName, reason, null, null)
        banManager.addBan(ban)
        source.sendSuccessMessage(Component.translatable("commands.banip.success", Component.text(ip), Component.text(ban.reason)))

        val playersToBan = getPlayersWithIp(source.server, ip)
        if (playersToBan.isNotEmpty()) {
            val names = Components.formatToList(playersToBan) { it.displayName }
            source.sendSuccessMessage(Component.translatable("commands.banip.info", Component.text(playersToBan.size), names))
        }
        playersToBan.forEach { it.disconnect(Component.translatable("multiplayer.disconnect.ip_banned")) }
        return playersToBan.size
    }

    private fun getPlayersWithIp(server: Server, ip: String): List<Player> = server.players.filter { AddressConverter.asString(it.address) == ip }

    companion object {

        private const val TARGET = "target"
        private const val REASON = "reason"

        private val ERROR_INVALID_IP = CommandExceptions.simple("commands.banip.invalid")
        private val ERROR_ALREADY_BANNED = CommandExceptions.simple("commands.banip.failed")
    }
}

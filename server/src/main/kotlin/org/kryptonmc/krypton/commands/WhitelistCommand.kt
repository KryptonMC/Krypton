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
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.gameProfileArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.server.whitelist.WhitelistEntry
import org.kryptonmc.krypton.server.whitelist.WhitelistIpEntry
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.matchesSubString
import org.kryptonmc.krypton.util.asString

object WhitelistCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("whitelist") {
            permission(KryptonPermission.WHITELIST)
            toggle("on", true, "enabled", "alreadyOn")
            toggle("off", false, "disabled", "alreadyOff")
            literal("list") {
                executes {
                    val sender = it.source
                    val server = it.source.server as? KryptonServer ?: return@executes 0
                    val whitelist = server.playerManager.whitelist
                    if (whitelist.isEmpty()) {
                        sender.sendMessage(Component.translatable("commands.whitelist.none"))
                        return@executes Command.SINGLE_SUCCESS
                    }
                    sender.sendMessage(Component.translatable(
                        "commands.whitelist.list",
                        Component.text(whitelist.size.toString()),
                        Component.text(whitelist.joinToString())
                    ))
                    Command.SINGLE_SUCCESS
                }
            }
            addOrRemove("add", true)
            addOrRemove("remove", false)
            literal("add-ip") {
                argument("target", StringArgumentType.string()) {
                    executes {
                        val server = it.source.server as? KryptonServer ?: return@executes 0
                        whitelistIp(server, it.argument("target"), it.source)
                        Command.SINGLE_SUCCESS
                    }
                }
            }
            literal("remove-ip") {
                argument("ip", StringArgumentType.string()) {
                    suggests { context, builder ->
                        val server = context.source.server as? KryptonServer ?: return@suggests builder.buildFuture()
                        server.playerManager.whitelistedIps.forEach {
                            if (!builder.remainingLowerCase.matchesSubString(it.key.lowercase())) return@forEach
                            builder.suggest(it.key)
                        }
                        builder.buildFuture()
                    }
                    executes {
                        val server = it.source.server as? KryptonServer ?: return@executes 0
                        val ip = it.argument<String>("ip")
                        if (server.playerManager.whitelistedIps.contains(ip)) {
                            server.playerManager.whitelistedIps.remove(ip)
                            it.source.sendMessage(Component.translatable("commands.whitelist.remove.success", Component.text(ip)))
                            return@executes Command.SINGLE_SUCCESS
                        }
                        it.source.sendMessage(Component.translatable("commands.whitelist.remove.failed"))
                        Command.SINGLE_SUCCESS
                    }
                }
            }
        })
    }

    @JvmStatic
    private fun whitelistIp(server: KryptonServer, target: String, sender: Sender) {
        if (target.matches(BanIpCommand.IP_ADDRESS_PATTERN)) {
            if (server.playerManager.whitelistedIps.contains(target)) {
                sender.sendMessage(Component.translatable("commands.whitelist.add.failed"))
                return
            }
            val entry = WhitelistIpEntry(target)
            server.playerManager.whitelistedIps.add(entry)
            sender.sendMessage(Component.translatable("commands.whitelist.add.success", Component.text(target)))
            return
        }
        val player = server.player(target)
        if (player != null) {
            val address = player.address.asString()
            if (server.playerManager.whitelistedIps.contains(address)) {
                sender.sendMessage(Component.translatable("commands.whitelist.add.failed"))
                return
            }
            val entry = WhitelistIpEntry(address)
            server.playerManager.whitelistedIps.add(entry)
            sender.sendMessage(Component.translatable("commands.whitelist.add.success", Component.text(target)))
            return
        }
        sender.sendMessage(Component.translatable("commands.banip.invalid"))
    }
}

private fun LiteralArgumentBuilder<Sender>.toggle(
    name: String,
    enable: Boolean,
    key: String,
    alreadyKey: String
): LiteralArgumentBuilder<Sender> = literal(name) {
    executes {
        val server = it.source.server as? KryptonServer ?: return@executes 0
        val toggled = if (enable) !server.playerManager.whitelistEnabled else server.playerManager.whitelistEnabled
        if (toggled) {
            server.playerManager.whitelistEnabled = enable
            it.source.sendMessage(Component.translatable("commands.whitelist.$key"))
        } else {
            it.source.sendMessage(Component.translatable("commands.whitelist.$alreadyKey"))
        }
        Command.SINGLE_SUCCESS
    }
}

private fun LiteralArgumentBuilder<Sender>.addOrRemove(name: String, add: Boolean): LiteralArgumentBuilder<Sender> = literal(name) {
    argument("targets", GameProfileArgument) {
        executes { context ->
            val server = context.source.server as? KryptonServer ?: return@executes 0
            val whitelist = server.playerManager.whitelist
            context.gameProfileArgument("targets").profiles(context.source).forEach {
                val state = if (add) !whitelist.contains(it) else whitelist.contains(it)
                if (state) {
                    if (add) whitelist.add(WhitelistEntry(it)) else whitelist.remove(it)
                    context.source.sendMessage(Component.translatable("commands.whitelist.$name.success", Component.text(it.name)))
                    return@forEach
                }
                context.source.sendMessage(Component.translatable("commands.whitelist.$name.failed"))
            }
            Command.SINGLE_SUCCESS
        }
    }
}

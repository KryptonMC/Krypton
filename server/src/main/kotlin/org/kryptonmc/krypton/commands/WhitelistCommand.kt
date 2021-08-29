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
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.gameProfileArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.suggest
import org.kryptonmc.krypton.server.whitelist.WhitelistEntry
import org.kryptonmc.krypton.server.whitelist.WhitelistIpEntry
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.util.asString

object WhitelistCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("whitelist")
            .permission("krypton.command.whitelist", 3)
            .then(literal<Sender>("on")
                .executes {
                    val sender = it.source
                    val server = sender.server as? KryptonServer ?: return@executes 0
                    if (!server.playerManager.whitelistEnabled) {
                        server.playerManager.whitelistEnabled = true
                        sender.sendMessage(translatable("commands.whitelist.enabled"))
                    } else {
                        sender.sendMessage(translatable("commands.whitelist.alreadyOn"))
                    }
                    1
                })
            .then(literal<Sender>("off")
                .executes {
                    val sender = it.source
                    val server = sender.server as? KryptonServer ?: return@executes 0
                    if (server.playerManager.whitelistEnabled) {
                        server.playerManager.whitelistEnabled = false
                        sender.sendMessage(translatable("commands.whitelist.disabled"))
                    } else {
                        sender.sendMessage(translatable("commands.whitelist.alreadyOff"))
                    }
                    1
                })
            .then(literal<Sender>("list")
                .executes {
                    val sender = it.source
                    val server = it.source.server as? KryptonServer ?: return@executes 0
                    val whitelist = server.playerManager.whitelist
                    if (whitelist.isEmpty()) {
                        sender.sendMessage(translatable("commands.whitelist.none"))
                    } else {
                        sender.sendMessage(translatable("commands.whitelist.list", text(whitelist.size.toString()), text(whitelist.joinToString())))
                    }
                    1
                })
            .then(literal<Sender>("add")
                .then(argument<Sender, EntityQuery>("targets", GameProfileArgument())
                    .executes {
                        val sender = it.source
                        val server = sender.server as? KryptonServer ?: return@executes 0
                        val targets = it.gameProfileArgument("targets").getProfiles(sender)
                        for (target in targets) {
                            val whitelist = server.playerManager.whitelist
                            if (!whitelist.contains(target)) {
                                whitelist.add(WhitelistEntry(target))
                                sender.sendMessage(translatable("commands.whitelist.add.success", text(target.name)))
                            } else {
                                sender.sendMessage(translatable("commands.whitelist.add.failed"))
                            }
                        }
                        1
                    })
            )
            .then(literal<Sender>("remove")
                .then(argument<Sender, EntityQuery>("targets", GameProfileArgument())
                    .executes {
                        val sender = it.source
                        val server = sender.server as? KryptonServer ?: return@executes 0
                        val targets = it.gameProfileArgument("targets").getProfiles(sender)
                        for (target in targets) {
                            val whitelist = server.playerManager.whitelist
                            if (whitelist.contains(target)) {
                                whitelist.remove(target)
                                sender.sendMessage(translatable("commands.whitelist.remove.success", text(target.name)))
                            } else {
                                sender.sendMessage(translatable("commands.whitelist.remove.failed"))
                            }
                        }
                        1
                    })
            ).then(literal<Sender>("add-ip")
                .then(argument<Sender, String>("target", string())
                    .executes {
                        val sender = it.source
                        val server = sender.server as? KryptonServer ?: return@executes 0
                        val target = it.argument<String>("target")
                        whitelistIp(server, target, sender)
                        1
                    })
            )
            .then(literal<Sender>("remove-ip")
                .then(argument<Sender, String>("ip", string())
                    .suggests { context, builder ->
                        builder.suggest((context.source.server as KryptonServer).playerManager.whitlistedIps.map { it.key })
                    }
                    .executes {
                        val sender = it.source
                        val server = sender.server as? KryptonServer ?: return@executes 0
                        val ip = it.argument<String>("ip")
                        if (server.playerManager.whitlistedIps.contains(ip)) {
                            server.playerManager.whitlistedIps.remove(ip)
                            sender.sendMessage(translatable("commands.whitelist.remove.success", text(ip)))
                        } else {
                            sender.sendMessage(translatable("commands.whitelist.remove.failed"))
                        }
                        1
                    })
            )
        )
    }

    private fun whitelistIp(server: KryptonServer, target: String, sender: Sender) {
        if (target.matches(BanIpCommand.IP_ADDRESS_PATTERN)) {
            if (server.playerManager.whitlistedIps.contains(target)) {
                sender.sendMessage(translatable("commands.whitelist.add.failed"))
            } else {
                val entry = WhitelistIpEntry(target)
                server.playerManager.whitlistedIps.add(entry)
                sender.sendMessage(translatable("commands.whitelist.add.success", text(target)))
            }
        } else if (server.player(target) != null) {
            val address = server.player(target)!!.address.asString()
            if (server.playerManager.whitlistedIps.contains(address)) {
                sender.sendMessage(translatable("commands.whitelist.add.failed"))
            } else {
                val entry = WhitelistIpEntry(address)
                server.playerManager.whitlistedIps.add(entry)
                sender.sendMessage(translatable("commands.whitelist.add.success", text(target)))
            }
        } else sender.sendMessage(translatable("commands.banip.invalid"))
    }
}

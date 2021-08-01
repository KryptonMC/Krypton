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
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.PermissionLevel
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
import org.kryptonmc.krypton.util.argument
import org.kryptonmc.krypton.util.stringify
import org.kryptonmc.krypton.util.toComponent

object WhitelistCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(
            literal<Sender>("whitelist")
                .permission("krypton.command.whitelist", PermissionLevel.LEVEL_3)
                .then(
                    literal<Sender>("on")
                        .executes {
                            val sender = it.source
                            val server = sender.server as KryptonServer
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
                        val server = sender.server as KryptonServer
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
                        val server = it.source.server as KryptonServer
                        val whitelist = server.playerManager.whitelist
                        if (whitelist.isEmpty()) {
                            sender.sendMessage(translatable("commands.whitelist.none"))
                        } else {
                            sender.sendMessage(
                                translatable(
                                    "commands.whitelist.list",
                                    listOf(
                                        whitelist.size.toString().toComponent(),
                                        whitelist.joinToString().toComponent()
                                    )
                                )
                            )
                        }
                        1
                    })
                .then(
                    literal<Sender>("add")
                        .then(argument<Sender, EntityQuery>("targets", GameProfileArgument.gameProfile())
                            .executes {
                                val sender = it.source
                                val server = sender.server as KryptonServer
                                val targets = it.gameProfileArgument("targets").getProfiles(sender)
                                for (target in targets) {
                                    val whitelist = server.playerManager.whitelist
                                    if (!whitelist.contains(target)) {
                                        whitelist += WhitelistEntry(target)
                                        sender.sendMessage(
                                            translatable(
                                                "commands.whitelist.add.success",
                                                listOf(target.name.toComponent())
                                            )
                                        )
                                    } else {
                                        sender.sendMessage(translatable("commands.whitelist.add.failed"))
                                    }
                                }
                                1
                            })
                )
                .then(
                    literal<Sender>("remove")
                        .then(argument<Sender, EntityQuery>("targets", GameProfileArgument.gameProfile())
                            .executes {
                                val sender = it.source
                                val server = sender.server as KryptonServer
                                val targets = it.gameProfileArgument("targets").getProfiles(sender)
                                for (target in targets) {
                                    val whitelist = server.playerManager.whitelist
                                    if (whitelist.contains(target)) {
                                        whitelist -= target
                                        sender.sendMessage(
                                            translatable(
                                                "commands.whitelist.remove.success",
                                                listOf(target.name.toComponent())
                                            )
                                        )
                                    } else {
                                        sender.sendMessage(translatable("commands.whitelist.remove.failed"))
                                    }
                                }
                                1
                            })
                )
                .then(
                    literal<Sender>("add-ip")
                        .then(argument<Sender, String>("target", string())
                            .executes {
                                val sender = it.source
                                val server = sender.server as KryptonServer
                                val target = it.argument<String>("target")
                                whitelistIp(server, target, sender)
                                1
                            })
                )
                .then(
                    literal<Sender>("remove-ip")
                        .then(argument<Sender, String>("ip", string())
                            .suggests { context, builder -> builder.suggest((context.source.server as KryptonServer).playerManager.whitlistedIps.map { it.key }) }
                            .executes {
                                val sender = it.source
                                val server = sender.server as KryptonServer
                                val ip = it.argument<String>("ip")
                                if (server.playerManager.whitlistedIps.contains(ip)) {
                                    server.playerManager.whitlistedIps -= ip
                                    sender.sendMessage(
                                        translatable(
                                            "commands.whitelist.remove.success",
                                            listOf(ip.toComponent())
                                        )
                                    )
                                } else {
                                    sender.sendMessage(translatable("commands.whitelist.remove.failed"))
                                }
                                1
                            })
                )
        )
    }

    private fun whitelistIp(server: KryptonServer, target: String, sender: Sender) {
        if (target.matches(BanIpCommand.PATTERN)) {
            if (server.playerManager.whitlistedIps.contains(target)) {
                sender.sendMessage(translatable("commands.whitelist.add.failed"))
            } else {
                val entry = WhitelistIpEntry(target)
                server.playerManager.whitlistedIps += entry
                sender.sendMessage(
                    translatable(
                        "commands.whitelist.add.success",
                        listOf(target.toComponent())
                    )
                )
            }
        } else if (server.player(target) != null) {
            val adress = server.player(target)!!.address.stringify()
            if (server.playerManager.whitlistedIps.contains(adress)) {
                sender.sendMessage(translatable("commands.whitelist.add.failed"))
            } else {
                val entry = WhitelistIpEntry(adress)
                server.playerManager.whitlistedIps += entry
                sender.sendMessage(
                    translatable(
                        "commands.whitelist.add.success",
                        listOf(target.toComponent())
                    )
                )
            }
        } else {
            sender.sendMessage(translatable("commands.banip.invalid"))
        }
    }

}

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
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.PermissionLevel
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument.Companion.players
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.argument
import java.time.Duration

object TitleCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(
            literal<Sender>("title")
                .permission("krypton.command.title", PermissionLevel.LEVEL_2)
                .then(
                    argument<Sender, EntityQuery>("targets", players())
                        .then(
                            literal<Sender>("actionbar")
                                .then(
                                    argument<Sender, String>("message", string())
                                        .executes {
                                        val sender = it.source as? KryptonPlayer ?: return@executes 1
                                        val targets = it.entityArgument("targets").getPlayers(sender)
                                        val message = it.argument<String>("message")
                                        for (target in targets) {
                                            target.sendActionBar(text(message))
                                        }
                                        val feedback = if (targets.size == 1) {
                                            translatable(
                                                "commands.title.show.actionbar.single",
                                                listOf(text(sender.name))
                                            )
                                        } else {
                                            translatable(
                                                "commands.title.show.actionbar.multiple",
                                                listOf(text(targets.size))
                                            )
                                        }
                                        sender.sendMessage(feedback)
                                        1
                                    })
                        )
                        .then(
                            literal<Sender>("title")
                                .then(argument<Sender, String>("message", string())
                                    .executes {
                                        val sender = it.source as? KryptonPlayer ?: return@executes 1
                                        val targets = it.entityArgument("targets").getPlayers(sender)
                                        val message = it.argument<String>("message")
                                        for (target in targets) {
                                            target.sendTitle(text(message))
                                        }
                                        val feedback = if (targets.size == 1) {
                                            translatable(
                                                "commands.title.show.title.single",
                                                listOf(text(sender.name))
                                            )
                                        } else {
                                            translatable(
                                                "commands.title.show.title.multiple",
                                                listOf(text(targets.size))
                                            )
                                        }
                                        sender.sendMessage(feedback)
                                        1
                                    })
                        ).then(
                            literal<Sender>("subtitle")
                                .then(argument<Sender, String>("message", string())
                                    .executes {
                                        val sender = it.source as? KryptonPlayer ?: return@executes 1
                                        val targets = it.entityArgument("targets").getPlayers(sender)
                                        val message = it.argument<String>("message")
                                        for (target in targets) {
                                            target.sendSubtitle(text(message))
                                        }
                                        val feedback = if (targets.size == 1) {
                                            translatable(
                                                "commands.title.show.subtitle.single",
                                                listOf(text(sender.name))
                                            )
                                        } else {
                                            translatable(
                                                "commands.title.show.subtitle.multiple",
                                                listOf(text(targets.size))
                                            )
                                        }
                                        sender.sendMessage(feedback)
                                        1
                                    })
                        ).then(
                            literal<Sender>("clear")
                                .executes {
                                    val sender = it.source as? KryptonPlayer ?: return@executes 1
                                    val targets = it.entityArgument("targets").getPlayers(sender)
                                    for (target in targets) {
                                        target.clearTitle()
                                    }
                                    val feedback = if (targets.size == 1) {
                                        translatable(
                                            "commands.title.cleared.single",
                                            listOf(text(sender.name))
                                        )
                                    } else {
                                        translatable(
                                            "commands.title.cleared.multiple",
                                            listOf(text(targets.size))
                                        )
                                    }
                                    sender.sendMessage(feedback)
                                    1
                                }
                        ).then(
                            literal<Sender>("reset")
                                .executes {
                                    val sender = it.source as? KryptonPlayer ?: return@executes 1
                                    val targets = it.entityArgument("targets").getPlayers(sender)
                                    for (target in targets) {
                                        target.resetTitle()
                                    }
                                    val feedback = if (targets.size == 1) {
                                        translatable(
                                            "commands.title.reset.single",
                                            listOf(text(sender.name))
                                        )
                                    } else {
                                        translatable(
                                            "commands.title.reset.multiple",
                                            listOf(text(targets.size))
                                        )
                                    }
                                    sender.sendMessage(feedback)
                                    1
                                })
                        .then(
                            literal<Sender>("times")
                                .then(
                                    argument<Sender, Int>("fadeIn", integer())
                                        .then(
                                            argument<Sender, Int>("stay", integer())
                                                .then(argument<Sender, Int>("fadeOut", integer())
                                                    .executes {
                                                        val sender = it.source as? KryptonPlayer ?: return@executes 1
                                                        val targets = it.entityArgument("targets").getPlayers(sender)
                                                        val fadeIn = it.argument<Int>("fadeIn")
                                                        val stay = it.argument<Int>("stay")
                                                        val fadeOut = it.argument<Int>("fadeOut")
                                                        for (target in targets) {
                                                            target.sendTitleTimes(
                                                                Duration.ofSeconds(fadeIn.toLong()),
                                                                Duration.ofSeconds(stay.toLong()),
                                                                Duration.ofSeconds(fadeOut.toLong())
                                                            )
                                                        }
                                                        val feedback = if (targets.size == 1) {
                                                            translatable(
                                                                "commands.title.times.single",
                                                                listOf(text(sender.name))
                                                            )
                                                        } else {
                                                            translatable(
                                                                "commands.title.times.multiple",
                                                                listOf(text(targets.size))
                                                            )
                                                        }
                                                        sender.sendMessage(feedback)
                                                        1
                                                    })
                                        )
                                )
                        )
                )
        )
    }
}

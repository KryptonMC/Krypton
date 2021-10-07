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
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument.Companion.players
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument
import java.time.Duration

object TitleCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("title")
            .permission("krypton.command.title", 2)
            .then(argument<Sender, EntityQuery>("targets", players())
                .then(literal<Sender>("actionbar")
                    .then(argument<Sender, String>("message", string())
                        .executes { context ->
                            val sender = context.source as? KryptonPlayer ?: return@executes 0
                            val targets = context.entityArgument("targets").players(sender)
                            val message = context.argument<String>("message")
                            targets.forEach { it.sendActionBar(text(message)) }
                            val feedback = if (targets.size == 1) {
                                translatable("commands.title.show.actionbar.single", text(sender.name))
                            } else {
                                translatable("commands.title.show.actionbar.multiple", text(targets.size))
                            }
                            sender.sendMessage(feedback)
                            1
                        })
                ).then(literal<Sender>("title")
                    .then(argument<Sender, String>("message", string())
                        .executes { context ->
                            val sender = context.source as? KryptonPlayer ?: return@executes 0
                            val targets = context.entityArgument("targets").players(sender)
                            val message = context.argument<String>("message")
                            targets.forEach { it.sendTitle(text(message)) }
                            val feedback = if (targets.size == 1) {
                                translatable("commands.title.show.title.single", text(sender.name))
                            } else {
                                translatable("commands.title.show.title.multiple", text(targets.size))
                            }
                            sender.sendMessage(feedback)
                            1
                        })
                ).then(literal<Sender>("subtitle")
                    .then(argument<Sender, String>("message", string())
                        .executes { context ->
                            val sender = context.source as? KryptonPlayer ?: return@executes 0
                            val targets = context.entityArgument("targets").players(sender)
                            val message = context.argument<String>("message")
                            targets.forEach { it.sendSubtitle(text(message)) }
                            val feedback = if (targets.size == 1) {
                                translatable("commands.title.show.subtitle.single", text(sender.name))
                            } else {
                                translatable("commands.title.show.subtitle.multiple", text(targets.size))
                            }
                            sender.sendMessage(feedback)
                            1
                        })
                ).then(literal<Sender>("clear")
                    .executes { context ->
                        val sender = context.source as? KryptonPlayer ?: return@executes 0
                        val targets = context.entityArgument("targets").players(sender)
                        targets.forEach { it.clearTitle() }
                        val feedback = if (targets.size == 1) {
                            translatable("commands.title.cleared.single", text(sender.name))
                        } else {
                            translatable("commands.title.cleared.multiple", text(targets.size))
                        }
                        sender.sendMessage(feedback)
                        1
                    }
                ).then(literal<Sender>("reset")
                    .executes { context ->
                        val sender = context.source as? KryptonPlayer ?: return@executes 0
                        val targets = context.entityArgument("targets").players(sender)
                        targets.forEach { it.resetTitle() }
                        val feedback = if (targets.size == 1) {
                            translatable("commands.title.reset.single", text(sender.name))
                        } else {
                            translatable("commands.title.reset.multiple", text(targets.size))
                        }
                        sender.sendMessage(feedback)
                        1
                    })
                .then(literal<Sender>("times")
                    .then(argument<Sender, Int>("fadeIn", integer())
                        .then(argument<Sender, Int>("stay", integer())
                            .then(argument<Sender, Int>("fadeOut", integer())
                                .executes { context ->
                                    val sender = context.source as? KryptonPlayer ?: return@executes 0
                                    val targets = context.entityArgument("targets").players(sender)
                                    val fadeIn = context.argument<Int>("fadeIn")
                                    val stay = context.argument<Int>("stay")
                                    val fadeOut = context.argument<Int>("fadeOut")
                                    targets.forEach {
                                        it.sendTitleTimes(
                                            Duration.ofSeconds(fadeIn.toLong()),
                                            Duration.ofSeconds(stay.toLong()),
                                            Duration.ofSeconds(fadeOut.toLong())
                                        )
                                    }
                                    val feedback = if (targets.size == 1) {
                                        translatable("commands.title.times.single", text(sender.name))
                                    } else {
                                        translatable("commands.title.times.multiple", text(targets.size))
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

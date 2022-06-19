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
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.entity.player.KryptonPlayer

object TitleCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("title") {
            permission(KryptonPermission.TITLE)
            argument("targets", EntityArgument.players()) {
                title("actionbar") { player, bar -> player.sendActionBar(bar) }
                title("title") { player, title -> player.sendTitle(title) }
                title("subtitle") { player, subtitle -> player.sendSubtitle(subtitle) }
                clearOrReset("clear", "cleared", Player::clearTitle)
                clearOrReset("reset", "reset", Player::resetTitle)
                times()
            }
        })
    }
}

private fun LiteralArgumentBuilder<Sender>.title(name: String, action: (KryptonPlayer, Component) -> Unit): LiteralArgumentBuilder<Sender> =
    literal(name) {
        argument("message", StringArgumentType.string()) {
            runs { context ->
                val sender = context.source as? KryptonPlayer ?: return@runs
                val targets = context.entityArgument("targets").players(sender)
                val message = Component.text(context.argument<String>("message"))
                targets.forEach { action(it, message) }
                sender.sendFeedback("commands.title.show.$name", targets.size)
            }
        }
    }

private fun LiteralArgumentBuilder<Sender>.clearOrReset(
    name: String,
    translationKey: String,
    action: (KryptonPlayer) -> Unit
): LiteralArgumentBuilder<Sender> = literal(name) {
    runs { context ->
        val sender = context.source as? KryptonPlayer ?: return@runs
        val targets = context.entityArgument("targets").players(sender)
        targets.forEach(action)
        sender.sendFeedback("commands.title.$translationKey", targets.size)
    }
}

private fun LiteralArgumentBuilder<Sender>.times(): LiteralArgumentBuilder<Sender> = literal("times") {
    argument("fadeIn", IntegerArgumentType.integer()) {
        argument("stay", IntegerArgumentType.integer()) {
            argument("fadeOut", IntegerArgumentType.integer()) {
                runs { context ->
                    val sender = context.source as? KryptonPlayer ?: return@runs
                    val targets = context.entityArgument("targets").players(sender)
                    targets.forEach { it.sendTitleTimes(context.argument("fadeIn"), context.argument("stay"), context.argument("fadeOut")) }
                    sender.sendFeedback("commands.title.times", targets.size)
                }
            }
        }
    }
}

private fun Player.sendFeedback(key: String, targets: Int) {
    val feedbackKey = if (targets == 1) "$key.single" else "$key.multiple"
    val feedbackArgument = if (targets == 1) displayName else Component.text(targets)
    sendMessage(Component.translatable(feedbackKey, feedbackArgument))
}

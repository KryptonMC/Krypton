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
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component.text
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.arguments.itemstack.ItemStackArgument
import org.kryptonmc.krypton.command.arguments.itemstack.ItemStackArgumentType
import org.kryptonmc.krypton.command.arguments.itemstack.itemStackArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutWindowItems
import org.kryptonmc.krypton.util.argument

object GiveCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("give")
            .permission("krypton.command.give", 2)
            .then(argument<Sender, EntityQuery>("targets", EntityArgument.players())
                .then(argument<Sender, ItemStackArgument>("item", ItemStackArgumentType())
                    .executes {
                        val targets = it.entityArgument("targets").getPlayers(it.source)
                        val item = it.itemStackArgument("item")
                        give(targets, item, 1, it.source.server as KryptonServer)
                        1
                    }
                    .then(argument<Sender, Int>("count", IntegerArgumentType.integer(1))
                        .executes {
                            val targets = it.entityArgument("targets").getPlayers(it.source)
                            val item = it.itemStackArgument("item")
                            val count = it.argument<Int>("count")
                            give(targets, item, count, it.source.server as KryptonServer)
                            1
                        }))))
    }

    private fun give(targets: List<KryptonPlayer>, item: ItemStackArgument, count: Int = 1, server: KryptonServer) {
        for (target in targets) {
            val items = item.createItemStacks(count, server)
            items.forEach { target.inventory += it }
            target.playSound(Sound.sound(key("entity.item.pickup"), Sound.Source.PLAYER, 0.2f, 2f))
            target.session.sendPacket(PacketOutWindowItems(target.inventory.id, target.inventory.stateId, target.inventory.networkItems, target.inventory.mainHand))
        }
    }
}

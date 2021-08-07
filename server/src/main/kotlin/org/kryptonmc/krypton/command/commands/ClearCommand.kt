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
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.PermissionLevel
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.arguments.itemstack.ItemStackPredicate
import org.kryptonmc.krypton.command.arguments.itemstack.ItemStackPredicateArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.EmptyItemStack
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.packet.out.play.PacketOutWindowItems
import org.kryptonmc.krypton.util.argument
import org.kryptonmc.krypton.util.toComponent

object ClearCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("clear")
            .permission("krypton.command.clear", PermissionLevel.LEVEL_2)
            .executes {
                if(it.source !is KryptonPlayer) return@executes 1
                clear(listOf(it.source as KryptonPlayer), it.source)
                1
            }
            .then(argument<Sender, EntityQuery>("targets", EntityArgument.players())
                .executes {
                    clear(it.entityArgument("targets").getPlayers(it.source), it.source)
                    1
                }
                .then(argument<Sender, ItemStackPredicate>("item", ItemStackPredicateArgument())
                    .executes {
                        clear(it.entityArgument("targets").getPlayers(it.source), it.source, it.argument("item"))
                        1
                    })))
    }

    private fun clear(targets: List<KryptonPlayer>, sender: Sender, predicate: ItemStackPredicate = ItemStackPredicate { true }, maxCount: Int = -1) {
        val amount = if(maxCount == -1) "all" else maxCount.toString()
        if(targets.size == 1) {
            val target = targets[0]
            clear(target, predicate, maxCount)
            sender.sendMessage(translatable("commands.clear.success.single", listOf(text(amount), target.name.toComponent())))
            target.session.sendPacket(PacketOutWindowItems(target.inventory.id, target.inventory.stateId, target.inventory.networkItems, target.inventory.mainHand))
            sender.sendMessage(translatable("commands.clear.success.single", listOf(text("all"), target.name.toComponent())))
        } else {
            for (target in targets) {
                for ((i, item) in target.inventory.withIndex()) {
                    if(predicate(item)) {
                        target.inventory[i] = EmptyItemStack
                    }
                }
                target.session.sendPacket(PacketOutWindowItems(target.inventory.id, target.inventory.stateId, target.inventory.networkItems, target.inventory.mainHand))
            }
            sender.sendMessage(translatable("commands.clear.success.multiple", listOf(text(amount), targets.size.toString().toComponent())))
        }
    }

    private fun clear(target: KryptonPlayer, predicate: ItemStackPredicate, maxCount: Int) {
        val inv = target.inventory
        var remaining = maxCount

        //Clear inventory items
        remaining = clearList(predicate, remaining, inv.items.withIndex()) { inv.items[it] = EmptyItemStack }
        if(remaining == 0) return

        //Clear armor items
        remaining = clearList(predicate, remaining, inv.armor.withIndex()) { inv.armor[it] = EmptyItemStack }
        if(remaining == 0) return

        //Clear crafting items
        remaining = clearList(predicate, remaining, inv.crafting.withIndex()) { inv.crafting[it] = EmptyItemStack }
        if(remaining == 0) return

        //Clear offhand
        clearList(predicate, remaining, listOf(inv.offHand).withIndex()) { inv.offHand = EmptyItemStack }
    }

    private fun clearList(predicate: ItemStackPredicate, originalRemaining: Int, withIndex: Iterable<IndexedValue<KryptonItemStack>>, removeItem: (Int) -> Unit) : Int {
        var remaining = originalRemaining
        for ((i, item) in withIndex) {
            if(predicate(item)) {
                if(remaining == -1) {
                    removeItem(i)
                } else if (remaining > item.amount) {
                    removeItem(i)
                    remaining -= item.amount
                } else {
                    item.amount -= remaining
                    remaining = 0
                    break
                }
            }
        }
        return remaining
    }

}

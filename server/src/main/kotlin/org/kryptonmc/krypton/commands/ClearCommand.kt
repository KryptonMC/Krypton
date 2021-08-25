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
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
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
import org.kryptonmc.krypton.command.argument.argument

object ClearCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("clear")
            .permission("krypton.command.clear", 2)
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

    //-1 means that everything should be cleared (there is no limit)
    private fun clear(targets: List<KryptonPlayer>, sender: Sender, predicate: ItemStackPredicate = ItemStackPredicate { true }, maxCount: Int = -1) {
        val amount = if(maxCount == -1) "all" else maxCount.toString()
        if (targets.size == 1) {
            val target = targets[0]
            clear(target, predicate, maxCount)
            sender.sendMessage(translatable("commands.clear.success.single", text(amount), text(target.name)))
            target.session.sendPacket(PacketOutWindowItems(target.inventory.id, target.inventory.incrementStateId(), target.inventory.networkWriter, target.inventory.mainHand))
        } else {
            targets.forEach { target ->
                target.inventory.forEachIndexed { index, item -> if (predicate(item)) target.inventory[index] = EmptyItemStack }
                target.session.sendPacket(PacketOutWindowItems(target.inventory.id, target.inventory.incrementStateId(), target.inventory.networkWriter, target.inventory.mainHand))
            }
            sender.sendMessage(translatable("commands.clear.success.multiple", text(amount), text(targets.size.toString())))
        }
    }

    private fun clear(target: KryptonPlayer, predicate: ItemStackPredicate, maxCount: Int) {
        val inv = target.inventory
        var remaining = maxCount

        //Clear inventory items
        remaining = clearList(predicate, remaining, inv.items) { inv.items[it] = EmptyItemStack }
        if (remaining == 0) return

        //Clear armor items
        remaining = clearList(predicate, remaining, inv.armor) { inv.armor[it] = EmptyItemStack }
        if (remaining == 0) return

        //Clear crafting items
        remaining = clearList(predicate, remaining, inv.crafting) { inv.crafting[it] = EmptyItemStack }
        if (remaining == 0) return

        //Clear offhand
        clearList(predicate, remaining, arrayOf(inv.offHand)) { inv.offHand = EmptyItemStack }
    }

    private fun clearList(predicate: ItemStackPredicate, originalRemaining: Int, items: Array<KryptonItemStack>, removeItem: (Int) -> Unit) : Int {
        var remaining = originalRemaining
        items.forEachIndexed { index, item ->
            if (!predicate(item)) return@forEachIndexed
            when {
                remaining == -1 -> removeItem(index)
                remaining > item.amount -> {
                    removeItem(index)
                    remaining -= item.amount
                }
                else -> {
                    item.amount -= remaining
                    remaining = 0
                    return remaining
                }
            }
        }
        return remaining
    }
}

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

import com.mojang.brigadier.CommandDispatcher
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.command.arguments.item.ItemStackPredicate
import org.kryptonmc.krypton.command.arguments.item.ItemStackPredicateArgument
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.locale.CommandMessages
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerContent
import java.util.function.Consumer

object ClearCommand {

    private const val TARGETS = "targets"
    private const val ITEM = "item"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literalCommand("clear") {
            requiresPermission(KryptonPermission.CLEAR)
            runs { clear(it.source, listOf(it.source.getPlayerOrError()), { true }, -1) }
            argument(TARGETS, EntityArgumentType.players()) {
                runs { clear(it.source, EntityArgumentType.getPlayers(it, TARGETS), { true }, -1) }
                argument(ITEM, ItemStackPredicateArgument) {
                    runs { clear(it.source, EntityArgumentType.getPlayers(it, TARGETS), it.getArgument(ITEM), -1) }
                }
            }
        })
    }

    //-1 means that everything should be cleared (there is no limit)
    @JvmStatic
    private fun clear(source: CommandSourceStack, targets: List<KryptonPlayer>, predicate: ItemStackPredicate, maxCount: Int) {
        val amount = if (maxCount == -1) "all" else maxCount.toString()
        if (targets.size == 1) {
            val target = targets.get(0)
            clear(target, predicate, maxCount)
            CommandMessages.CLEAR_SINGLE.sendSuccess(source, amount, target.displayName, true)
            target.connection.send(PacketOutSetContainerContent.fromPlayerInventory(target.inventory))
        } else {
            targets.forEach { target ->
                target.inventory.items.forEachIndexed { index, item ->
                    if (predicate.test(item)) target.inventory.setItem(index, KryptonItemStack.EMPTY)
                }
                target.connection.send(PacketOutSetContainerContent.fromPlayerInventory(target.inventory))
            }
            CommandMessages.CLEAR_MULTIPLE.sendSuccess(source, amount, targets.size, true)
        }
    }

    @JvmStatic
    private fun clear(target: KryptonPlayer, predicate: ItemStackPredicate, maxCount: Int) {
        val inventory = target.inventory
        var remaining = maxCount
        // Clear inventory items
        remaining = clearList(predicate, remaining, inventory.items)
        if (remaining == 0) return
        // Clear armor items
        remaining = clearList(predicate, remaining, inventory.armor)
        if (remaining == 0) return
        // Clear crafting items
        remaining = clearList(predicate, remaining, inventory.crafting)
        if (remaining == 0) return
        // Clear offhand
        clearItem(predicate, remaining, inventory.offHand) { inventory.offHand = it }
    }

    @JvmStatic
    private fun clearList(predicate: ItemStackPredicate, originalRemaining: Int, items: MutableList<KryptonItemStack>): Int {
        var remaining = originalRemaining
        items.forEachIndexed { index, item ->
            val newRemaining = clearItem(predicate, remaining, item) { items.set(index, it) }
            if (newRemaining == -1) return@forEachIndexed
            if (newRemaining == 0) return 0
            remaining = newRemaining
        }
        return remaining
    }

    @JvmStatic
    private fun clearItem(predicate: ItemStackPredicate, remaining: Int, item: KryptonItemStack, setItem: Consumer<KryptonItemStack>): Int {
        if (!predicate.test(item)) return -1
        return when {
            remaining == -1 -> {
                setItem.accept(KryptonItemStack.EMPTY)
                remaining
            }
            remaining > item.amount -> {
                setItem.accept(KryptonItemStack.EMPTY)
                remaining - item.amount
            }
            else -> {
                setItem.accept(item.shrink(remaining))
                0
            }
        }
    }
}

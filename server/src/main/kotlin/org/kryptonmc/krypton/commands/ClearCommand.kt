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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.arguments.item.ItemStackPredicate
import org.kryptonmc.krypton.command.arguments.item.ItemStackPredicateArgument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerContent
import java.util.function.Consumer

object ClearCommand : InternalCommand {

    private const val TARGETS_ARGUMENT = "targets"

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("clear") {
            permission(KryptonPermission.CLEAR)
            runs {
                val player = it.source as? KryptonPlayer ?: return@runs
                clear(listOf(player), player)
            }
            argument(TARGETS_ARGUMENT, EntityArgumentType.players()) {
                runs { clear(it.entityArgument(TARGETS_ARGUMENT).players(it.source), it.source) }
                argument("item", ItemStackPredicateArgument) {
                    runs { clear(it.entityArgument(TARGETS_ARGUMENT).players(it.source), it.source, it.argument("item")) }
                }
            }
        })
    }

    //-1 means that everything should be cleared (there is no limit)
    @JvmStatic
    private fun clear(targets: List<KryptonPlayer>, sender: Sender, predicate: ItemStackPredicate = ItemStackPredicate { true }, maxCount: Int = -1) {
        val amount = if (maxCount == -1) "all" else maxCount.toString()
        if (targets.size == 1) {
            val target = targets.get(0)
            clear(target, predicate, maxCount)
            sender.sendMessage(Component.translatable("commands.clear.success.single", Component.text(amount), target.displayName))
            target.session.send(PacketOutSetContainerContent(target.inventory, target.inventory.mainHand))
        } else {
            targets.forEach { target ->
                target.inventory.items.forEachIndexed { index, item -> if (predicate(item)) target.inventory.set(index, KryptonItemStack.EMPTY) }
                target.session.send(PacketOutSetContainerContent(target.inventory, target.inventory.mainHand))
            }
            val targetSize = Component.text(targets.size.toString())
            sender.sendMessage(Component.translatable("commands.clear.success.multiple", Component.text(amount), targetSize))
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
        if (!predicate(item)) return -1
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

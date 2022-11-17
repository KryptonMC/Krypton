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
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.command.arguments.entityArgument
import org.kryptonmc.krypton.command.arguments.item.ItemStackArgument
import org.kryptonmc.krypton.command.arguments.item.ItemStackArgumentType
import org.kryptonmc.krypton.command.arguments.itemStackArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerContent
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs

object GiveCommand : InternalCommand {

    // These constants come from vanilla
    private const val PICKUP_VOLUME = 0.2F
    private const val PICKUP_PITCH = 2F

    private const val TARGETS_ARGUMENT = "targets"
    private const val ITEM_ARGUMENT = "item"

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("give") {
            permission(KryptonPermission.GIVE)
            argument(TARGETS_ARGUMENT, EntityArgumentType.players()) {
                argument(ITEM_ARGUMENT, ItemStackArgumentType) {
                    runs { give(it.entityArgument(TARGETS_ARGUMENT).players(it.source), it.itemStackArgument(ITEM_ARGUMENT), 1) }
                    argument("count", IntegerArgumentType.integer(1)) {
                        runs {
                            give(it.entityArgument(TARGETS_ARGUMENT).players(it.source), it.itemStackArgument(ITEM_ARGUMENT), it.argument("count"))
                        }
                    }
                }
            }
        })
    }

    @JvmStatic
    private fun give(targets: List<KryptonPlayer>, item: ItemStackArgument, count: Int) {
        targets.forEach { target ->
            item.createItemStacks(count).forEach(target.inventory::add)
            target.playSound(Sound.sound(SoundEvents.ITEM_PICKUP, Sound.Source.PLAYER, PICKUP_VOLUME, PICKUP_PITCH))
            target.session.send(PacketOutSetContainerContent(target.inventory, target.inventory.mainHand))
        }
    }
}

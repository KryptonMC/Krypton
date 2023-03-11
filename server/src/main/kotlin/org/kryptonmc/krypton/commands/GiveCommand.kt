/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.command.arguments.item.ItemStackArgument
import org.kryptonmc.krypton.command.arguments.item.ItemStackArgumentType
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerContent

object GiveCommand {

    // These constants come from vanilla
    private const val PICKUP_VOLUME = 0.2F
    private const val PICKUP_PITCH = 2F

    private const val TARGETS = "targets"
    private const val ITEM = "item"
    private const val COUNT = "count"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literalCommand("give") {
            requiresPermission(KryptonPermission.GIVE)
            argument(TARGETS, EntityArgumentType.players()) {
                argument(ITEM, ItemStackArgumentType) {
                    runs { give(EntityArgumentType.getPlayers(it, TARGETS), ItemStackArgumentType.get(it, ITEM), 1) }
                    argument(COUNT, IntegerArgumentType.integer(1)) {
                        runs {
                            give(EntityArgumentType.getPlayers(it, TARGETS), ItemStackArgumentType.get(it, ITEM), it.getArgument(COUNT))
                        }
                    }
                }
            }
        })
    }

    @JvmStatic
    private fun give(targets: List<KryptonPlayer>, item: ItemStackArgument, count: Int) {
        targets.forEach { target ->
            item.createItemStacks(count).forEach(target.inventory::addItem)
            target.playSound(Sound.sound(SoundEvents.ITEM_PICKUP, Sound.Source.PLAYER, PICKUP_VOLUME, PICKUP_PITCH))
            target.connection.send(PacketOutSetContainerContent.fromPlayerInventory(target.inventory))
        }
    }
}

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
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.SuggestionProviders
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.NBTCompoundArgument
import org.kryptonmc.krypton.command.arguments.SummonEntityArgument
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.summonableEntity
import org.kryptonmc.krypton.command.arguments.vectorArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.command.toExceptionType
import org.kryptonmc.krypton.util.isInSpawnableBounds
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3d

object SummonCommand : InternalCommand {

    private val ERROR_FAILED = Component.translatable("commands.summon.failed").toExceptionType()
    private val ERROR_INVALID_POSITION = Component.translatable("commands.summon.invalidPosition").toExceptionType()

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("summon") {
            permission(KryptonPermission.SUMMON)
            argument("entity", SummonEntityArgument) {
                suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                runs {
                    val sender = it.source as? KryptonPlayer ?: return@runs
                    spawnEntity(sender, it.summonableEntity("entity"), sender.location)
                }
                argument("position", VectorArgument.normal()) {
                    runs {
                        val sender = it.source as? KryptonPlayer ?: return@runs
                        spawnEntity(sender, it.summonableEntity("entity"), it.vectorArgument("position"))
                    }
                    argument("nbt", NBTCompoundArgument) {
                        runs {
                            val sender = it.source as? KryptonPlayer ?: return@runs
                            spawnEntity(sender, it.summonableEntity("entity"), it.vectorArgument("position"), it.argument("nbt"))
                        }
                    }
                }
            }
        })
    }

    @JvmStatic
    private fun spawnEntity(player: KryptonPlayer, entityType: Key, position: Vector3d, nbt: CompoundTag? = CompoundTag.empty()) {
        if (!position.isInSpawnableBounds()) throw ERROR_INVALID_POSITION.create()
        val world = player.world
        val entity = EntityFactory.create(world, entityType.asString(), nbt)?.apply { this.location = position } ?: throw ERROR_FAILED.create()
        world.spawnEntity(entity)
        player.sendMessage(Component.translatable("commands.summon.success", entity.displayName))
    }
}

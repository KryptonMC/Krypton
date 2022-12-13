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
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.SuggestionProviders
import org.kryptonmc.krypton.command.arguments.NBTCompoundArgument
import org.kryptonmc.krypton.command.arguments.SummonEntityArgument
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.Worlds
import org.kryptonmc.nbt.CompoundTag

object SummonCommand {

    private val ERROR_FAILED = CommandExceptions.simple("commands.summon.failed")
    private val ERROR_INVALID_POSITION = CommandExceptions.simple("commands.summon.invalidPosition")

    private const val ENTITY = "entity"
    private const val POSITION = "position"
    private const val NBT = "nbt"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("summon") {
            permission(KryptonPermission.SUMMON)
            argument(ENTITY, SummonEntityArgument) {
                suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                runs { spawnEntity(it.source, SummonEntityArgument.get(it, ENTITY), it.source.position, CompoundTag.EMPTY) }
                argument(POSITION, VectorArgument.normal()) {
                    runs { spawnEntity(it.source, SummonEntityArgument.get(it, ENTITY), VectorArgument.get(it, POSITION), CompoundTag.EMPTY) }
                    argument(NBT, NBTCompoundArgument) {
                        runs {
                            spawnEntity(it.source, SummonEntityArgument.get(it, ENTITY), VectorArgument.get(it, POSITION), it.getArgument(NBT))
                        }
                    }
                }
            }
        })
    }

    @JvmStatic
    private fun spawnEntity(source: CommandSourceStack, type: Key, position: Vec3d, nbt: CompoundTag) {
        if (!Worlds.isInSpawnableBounds(position)) throw ERROR_INVALID_POSITION.create()
        val entity = EntityFactory.create(source.world, type.asString(), nbt)?.apply { this.position = position } ?: throw ERROR_FAILED.create()
        source.world.spawnEntity(entity)
        source.sendSuccess(Messages.Commands.SUMMON_SUCCESS.build(entity.displayName), true)
    }
}

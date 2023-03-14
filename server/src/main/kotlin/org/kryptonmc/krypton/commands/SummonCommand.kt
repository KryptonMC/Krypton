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
import net.kyori.adventure.key.Key
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.SuggestionProviders
import org.kryptonmc.krypton.command.arguments.NBTCompoundArgument
import org.kryptonmc.krypton.command.arguments.SummonEntityArgument
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.locale.CommandMessages
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
        dispatcher.register(literalCommand("summon") {
            requiresPermission(KryptonPermission.SUMMON)
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
    private fun spawnEntity(source: CommandSourceStack, type: Key, position: Position, nbt: CompoundTag) {
        if (!Worlds.isInSpawnableBounds(position)) throw ERROR_INVALID_POSITION.create()
        val entity = EntityFactory.create(source.world, type.asString(), nbt)?.apply { this.position = position } ?: throw ERROR_FAILED.create()
        source.world.spawnEntity(entity)
        CommandMessages.SUMMON.sendSuccess(source, entity.displayName, true)
    }
}

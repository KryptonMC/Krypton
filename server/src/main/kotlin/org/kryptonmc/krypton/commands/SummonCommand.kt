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
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.util.Vec3d
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
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.Worlds
import org.kryptonmc.nbt.CompoundTag

object SummonCommand : InternalCommand {

    private val ERROR_FAILED = CommandExceptions.simple("commands.summon.failed")
    private val ERROR_INVALID_POSITION = CommandExceptions.simple("commands.summon.invalidPosition")

    private const val ENTITY_ARGUMENT = "entity"
    private const val POSITION_ARGUMENT = "position"

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("summon") {
            permission(KryptonPermission.SUMMON)
            argument(ENTITY_ARGUMENT, SummonEntityArgument) {
                suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                runs { spawnEntity(it.source, it.summonableEntity(ENTITY_ARGUMENT), null, null) }
                argument(POSITION_ARGUMENT, VectorArgument.normal()) {
                    runs { spawnEntity(it.source, it.summonableEntity(ENTITY_ARGUMENT), it.vectorArgument(POSITION_ARGUMENT), null) }
                    argument("nbt", NBTCompoundArgument) {
                        runs {
                            spawnEntity(it.source, it.summonableEntity(ENTITY_ARGUMENT), it.vectorArgument(POSITION_ARGUMENT), it.argument("nbt"))
                        }
                    }
                }
            }
        })
    }

    @JvmStatic
    private fun spawnEntity(sender: Sender, type: Key, pos: Vec3d?, nbt: CompoundTag?) {
        val player = sender as? KryptonPlayer ?: return
        val position = pos ?: player.location
        if (!Worlds.isInSpawnableBounds(position)) throw ERROR_INVALID_POSITION.create()
        val entity = EntityFactory.create(player.world, type.asString(), nbt)?.apply { this.location = position } ?: throw ERROR_FAILED.create()
        player.world.spawnEntity(entity)
        Messages.Commands.SUMMON_SUCCESS.send(player, entity.displayName)
    }
}

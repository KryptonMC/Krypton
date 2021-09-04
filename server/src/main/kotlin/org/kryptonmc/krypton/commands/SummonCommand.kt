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
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.space.Position
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.SuggestionProviders
import org.kryptonmc.krypton.command.arguments.NBTCompoundArgument
import org.kryptonmc.krypton.command.arguments.SummonEntityArgument
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates
import org.kryptonmc.krypton.command.arguments.summonableEntity
import org.kryptonmc.krypton.command.arguments.vectorArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.util.isInSpawnableBounds
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.MutableCompoundTag

object SummonCommand : InternalCommand {

    private val ERROR_FAILED = SimpleCommandExceptionType(translatable("commands.summon.failed").toMessage())
    private val ERROR_INVALID_POSITION = SimpleCommandExceptionType(
        translatable("commands.summon.invalidPosition").toMessage()
    )

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("summon")
            .permission("krypton.command.summon", 2)
            .then(argument<Sender, Key>("entity", SummonEntityArgument())
                .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                .executes {
                    val sender = it.source as? KryptonPlayer ?: return@executes 0
                    spawnEntity(sender, it.summonableEntity("entity"), sender.location)
                    1
                }.then(argument<Sender, Coordinates>("position", VectorArgument())
                    .executes {
                        val sender = it.source as? KryptonPlayer ?: return@executes 0
                        spawnEntity(
                            sender,
                            it.summonableEntity("entity"),
                            it.vectorArgument("position")
                        )
                        1
                    }.then(argument<Sender, CompoundTag>("nbt", NBTCompoundArgument())
                        .executes {
                            val sender = it.source as? KryptonPlayer ?: return@executes 0
                            spawnEntity(
                                sender,
                                it.summonableEntity("entity"),
                                it.vectorArgument("position"),
                                it.argument<CompoundTag>("nbt")
                            )
                            1
                        })))
        )
    }

    private fun spawnEntity(
        player: KryptonPlayer,
        entityType: Key,
        position: Position,
        nbt: CompoundTag? = MutableCompoundTag()
    ) {
        if (!position.isInSpawnableBounds()) throw ERROR_INVALID_POSITION.create()
        val world = player.world
        val entity = EntityFactory.create(world, entityType.asString(), nbt)?.apply {
            location = location.copy(position.x, position.y, position.z)
        } ?: throw ERROR_FAILED.create()

        world.spawnEntity(entity)
        player.sendMessage(translatable("commands.summon.success", entity.displayName))
    }
}

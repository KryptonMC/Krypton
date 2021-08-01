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
package org.kryptonmc.krypton.command.arguments.entities

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.BrigadierExceptions
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.toIntRange

/**
 * Documentation [TODO]
 */
class EntityQuery(
    private val args: List<EntityArgument.EntityArg>,
    val type: SELECTOR,
    private val playerName: String = ""
) {

    fun getEntities(source: KryptonPlayer) = when (type) {
        SELECTOR.RANDOM_PLAYER -> {
            listOf(source.server.players.random())
        }
        SELECTOR.ALL_PLAYERS -> {
            if (args.isNotEmpty()) {
                val entities = applyArguments((source.server.players + source.world.entities).toList(), source)
                if (entities.isEmpty()) throw PLAYER_NOT_FOUND.create()
                entities
            } else {
                source.server.players
            }
        }
        SELECTOR.EXECUTOR -> listOf(source)
        SELECTOR.ALL_ENTITIES -> {
            if (args.isNotEmpty()) {
                val entities = applyArguments((source.server.players + source.world.entities).toList(), source)
                if (entities.isEmpty()) throw ENTITY_NOT_FOUND.create()
                entities
            } else {
                (source.server.players + source.world.entities).toList()
            }
        }
        SELECTOR.NEAREST_PLAYER -> {
            var currentNearest = source.server.players[0]
            for (player in source.server.players) {
                if (player.distance(source) < currentNearest.distance(source)) currentNearest = player
            }
            listOf(currentNearest)
        }
        SELECTOR.PLAYER -> listOf(source.server.player(playerName) ?: throw PLAYER_NOT_EXISTS.create())
        SELECTOR.UNKNOWN -> throw UNKNOWN_SELECTOR_EXCEPTION.create("")
    }

    fun getEntity(source: KryptonPlayer): KryptonEntity = getEntities(source)[0]

    fun getPlayers(source: KryptonPlayer) =
        getEntities(source).map { if (it !is KryptonPlayer) throw UnsupportedOperationException("You cannot call .getPlayers() if there is an entity in the arguments") else it }

    fun getPlayer(server: KryptonServer) = if (playerName.isNotEmpty()) {
        server.player(playerName) ?: throw PLAYER_NOT_FOUND.create()
    } else {
        throw PLAYER_NOT_FOUND.create()
    }

    fun getProfiles(source: KryptonPlayer) = if (playerName.isNotEmpty()) {
        listOf(source.server.playerManager.userCache.getProfileByName(playerName) ?: throw PLAYER_NOT_FOUND.create())
    } else {
        getPlayers(source).map { it.profile }
    }

    fun getProfile(server: KryptonServer) = if (playerName.isNotEmpty()) {
        server.playerManager.userCache.getProfileByName(playerName) ?: throw PLAYER_NOT_FOUND.create()
    } else {
        throw PLAYER_NOT_FOUND.create()
    }

    private fun applyArguments(originalEntities: List<KryptonEntity>, source: KryptonPlayer): List<KryptonEntity> {
        var entities = originalEntities
        var differenceX = 0
        var differenceY = 0
        var differenceZ = 0
        for ((option, value, exclude) in args) {
            when (option) {
                "dx" -> {
                    checkInt(value.toString())
                    differenceX = value.toString().toInt()
                }
                "dy" -> {
                    checkInt(value.toString())
                    differenceY = value.toString().toInt()
                }
                "dz" -> {
                    checkInt(value.toString())
                    differenceZ = value.toString().toInt()
                }
                "x" -> {
                    checkInt(value.toString())
                    entities = entities.filter {
                        applyDifference(differenceX, value, it.location.blockX)
                    }
                }
                "y" -> {
                    checkInt(value.toString())
                    entities = entities.filter {
                        applyDifference(differenceY, value, it.location.blockY)
                    }
                }
                "z" -> {
                    checkInt(value.toString())
                    entities = entities.filter {
                        applyDifference(differenceZ, value, it.location.blockZ)
                    }
                }
                "distance" -> {
                    checkIntOrRange(value.toString())
                    entities = if (value.toString().startsWith("..")) {
                        val distance = value.toString().replace("..", "").toInt()
                        entities.filter {
                            it.distance(source) <= distance
                        }
                    } else if (!value.toString().contains("..")) {
                        checkInt(value.toString())
                        entities.filter {
                            val int = it.distance(source).toInt()
                            if (int < 0) throw DISTANCE_NEGATIVE.create()
                            int == value.toString().toInt()
                        }
                    } else {
                        val range = value.toString().toIntRange()
                        entities.filter {
                            it.distance(source) >= range!!.first && it.distance(source) <= range.last
                        }
                    }
                }
                "scores" -> {
                    notImplemented("scores")
                }
                "tag" -> {
                    notImplemented("tag")
                }
                "team" -> {
                    notImplemented("team")
                }
                "level" -> {
                    notImplemented("level")
                }
                "gamemode" -> {
                    entities = entities.filter {
                        if (it !is KryptonPlayer) return@filter true
                        if (exclude) {
                            it.gamemode != Gamemode.fromName(value.toString())
                        } else {
                            it.gamemode == Gamemode.fromName(value.toString())
                        }
                    }
                }
                "name" -> {
                    entities = entities.filter {
                        if (exclude) it.name != value else it.name == value
                    }
                }
                "x_rotation" -> {
                    checkIntOrRange(value.toString())
                    entities = if (value.toString().startsWith("..")) {
                        val pitch = value.toString().replace("..", "").toInt()
                        entities.filter {
                            it.location.pitch <= pitch
                        }
                    } else if (!value.toString().contains("..")) {
                        entities.filter {
                            it.location.pitch.toInt() == value.toString().toInt()
                        }
                    } else {
                        val range = value.toString().toIntRange()
                        entities.filter {
                            it.location.pitch >= range!!.first && it.location.pitch <= range.last
                        }
                    }
                }
                "y_rotation" -> {
                    checkIntOrRange(value.toString())
                    entities = if (value.toString().startsWith("..")) {
                        val yaw = value.toString().replace("..", "").toInt()
                        entities.filter {
                            it.location.yaw <= yaw
                        }
                    } else if (!value.toString().contains("..")) {
                        entities.filter {
                            it.location.yaw.toInt() == value.toString().toInt()
                        }
                    } else {
                        val range = value.toString().toIntRange()
                        entities.filter {
                            it.location.yaw >= range!!.first && it.location.yaw <= range.last
                        }
                    }
                }
                "type" -> {
                    notImplemented("type")
                }
                "nbt" -> {
                    notImplemented("nbt")
                }
                "advancements" -> {
                    notImplemented("advancements")
                }
                "predicate" -> {
                    notImplemented("predicate")
                }
                "sort" -> {
                    val sorter = EntityArguments.Sorter.fromName(value.toString())
                        ?: throw INVALID_SORT_TYPE.create(value)
                    entities = when (sorter) {
                        EntityArguments.Sorter.NEAREST -> entities.sortedBy { it.distance(source) }
                        EntityArguments.Sorter.FURTHEST -> entities.sortedByDescending { it.distance(source) }
                        EntityArguments.Sorter.RANDOM -> entities.shuffled()
                        EntityArguments.Sorter.ARBITRARY -> entities.sortedBy { it.ticksLived }
                    }
                }
                "limit" -> {
                    checkInt(value.toString())
                    val limit = value.toString().toInt()
                    if (limit <= 0) throw LIMIT_NULL.create()
                    entities = if (entities.size > limit) entities.subList(0, limit - 1) else entities
                }
                else -> {
                    throw UnsupportedOperationException("Invalid option")
                }
            }
        }
        return entities
    }

    private fun applyDifference(difference: Int, value: Any, blockCoordinate: Int) = if (difference != 0) {
        val min = value.toString().toInt()
        val max = min + difference
        blockCoordinate in min..max
    } else {
        blockCoordinate == value.toString().toInt()
    }

    private fun notImplemented(option: String) {
        throw DynamicCommandExceptionType { a ->
            text("Not yet implemented: $a").toMessage()
        }.create(option)
    }

    private fun checkInt(string: String) =
        string.toIntOrNull() ?: throw BrigadierExceptions.readerExpectedInt().create()

    private fun checkIntOrRange(string: String) {
        if (string.toIntOrNull() != null) return
        if (string.startsWith("..") && string.substring(2).toIntOrNull() != null) return
        if (string.contains("..") && string.toIntRange() != null) return
        throw SimpleCommandExceptionType(translatable("argument.range.empty").toMessage()).create()
    }

    enum class SELECTOR {

        RANDOM_PLAYER,
        ALL_PLAYERS,
        EXECUTOR,
        ALL_ENTITIES,
        NEAREST_PLAYER,
        PLAYER,
        UNKNOWN;

        companion object {

            /**
             * Get the target selector from it's short name
             * you can find these at the [Minecraft Wiki](https://minecraft.fandom.com/wiki/Target_selectors)
             */
            fun fromChar(selector: Char) = when (selector) {
                'p' -> NEAREST_PLAYER
                'r' -> RANDOM_PLAYER
                'a' -> ALL_PLAYERS
                'e' -> ALL_ENTITIES
                's' -> EXECUTOR
                else -> UNKNOWN
            }
        }

    }

}



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
import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.command.BrigadierExceptions
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.KryptonGameMode

/**
 * Documentation [TODO]
 */
@JvmRecord
data class EntityQuery(
    private val args: List<EntityArgument.EntityArg>,
    val type: Selector,
    private val playerName: String = ""
) {

    fun entities(source: KryptonPlayer) = when (type) {
        Selector.RANDOM_PLAYER -> listOf(source.server.players.random())
        Selector.ALL_PLAYERS -> {
            if (args.isNotEmpty()) {
                val entities = applyArguments((source.server.players + source.world.entities).toList(), source)
                if (entities.isEmpty()) throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create()
                entities
            } else {
                source.server.players
            }
        }
        Selector.EXECUTOR -> listOf(source)
        Selector.ALL_ENTITIES -> {
            if (args.isNotEmpty()) {
                val entities = applyArguments((source.server.players + source.world.entities).toList(), source)
                if (entities.isEmpty()) throw EntityArgumentExceptions.ENTITY_NOT_FOUND.create()
                entities
            } else {
                source.server.players + source.world.entities
            }
        }
        Selector.NEAREST_PLAYER -> {
            var currentNearest = source.server.players[0]
            source.server.players.forEach {
                if (it.location.distanceSquared(source.location) < currentNearest.location.distanceSquared(source.location)) currentNearest = it
            }
            listOf(currentNearest)
        }
        Selector.PLAYER -> listOf(source.server.player(playerName)
            ?: throw EntityArgumentExceptions.UNKNOWN_PLAYER.create())
        Selector.UNKNOWN -> throw EntityArgumentExceptions.UNKNOWN_SELECTOR.create("")
    }

    fun entity(source: KryptonPlayer): KryptonEntity = entities(source)[0]

    fun players(sender: Sender): List<KryptonPlayer> {
        val server = sender.server as KryptonServer
        return if (sender is KryptonPlayer) {
            if (playerName.isNotEmpty()) return listOf(server.player(playerName)
                ?: throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create())
            entities(sender).map {
                it as? KryptonPlayer ?: throw UnsupportedOperationException("You cannot call .getPlayers() if there " +
                        "is an entity in the arguments")
            }
        } else {
            listOf(server.player(playerName) ?: throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create())
        }
    }

    fun profiles(sender: Sender): List<KryptonGameProfile> {
        val server = sender.server as? KryptonServer ?: return emptyList()
        if (sender is KryptonPlayer) {
            if (playerName.isNotEmpty()) return listOf(server.profileCache[playerName] ?: throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create())
            return players(sender).map { it.profile }
        }
        if (playerName.isNotEmpty()) return listOf(server.profileCache[playerName] ?: throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create())
        return emptyList()
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
                    entities = entities.filter { applyDifference(differenceX, value, it.location.floorX()) }
                }
                "y" -> {
                    checkInt(value.toString())
                    entities = entities.filter { applyDifference(differenceY, value, it.location.floorY()) }
                }
                "z" -> {
                    checkInt(value.toString())
                    entities = entities.filter { applyDifference(differenceZ, value, it.location.floorZ()) }
                }
                "distance" -> {
                    checkIntOrRange(value.toString())
                    entities = if (value.toString().startsWith("..")) {
                        val distance = value.toString().replace("..", "").toInt()
                        entities.filter { it.location.distanceSquared(source.location) <= distance }
                    } else if (!value.toString().contains("..")) {
                        checkInt(value.toString())
                        entities.filter {
                            val int = it.location.distanceSquared(source.location).toInt()
                            if (int < 0) throw EntityArgumentExceptions.DISTANCE_NEGATIVE.create()
                            int == value.toString().toInt()
                        }
                    } else {
                        val range = value.toString().toIntRange()
                        entities.filter {
                            it.location.distanceSquared(source.location) >= range!!.first &&
                                    it.location.distanceSquared(source.location) <= range.last
                        }
                    }
                }
                "scores" -> notImplemented("scores")
                "tag" -> notImplemented("tag")
                "team" -> notImplemented("team")
                "level" -> notImplemented("level")
                "gamemode" -> {
                    entities = entities.filter {
                        if (it !is KryptonPlayer) return@filter true
                        if (exclude) {
                            it.gameMode !== KryptonGameMode.fromName(value.toString())
                        } else {
                            it.gameMode === KryptonGameMode.fromName(value.toString())
                        }
                    }
                }
                "name" -> entities = entities.filter {
                    val name = if (it is Player) it.profile.name else it.name.toPlainText()
                    if (exclude) name != value else name == value
                }
                "x_rotation" -> {
                    checkIntOrRange(value.toString())
                    entities = if (value.toString().startsWith("..")) {
                        val pitch = value.toString().replace("..", "").toInt()
                        entities.filter { it.rotation.y() <= pitch }
                    } else if (!value.toString().contains("..")) {
                        entities.filter { it.rotation.y().toInt() == value.toString().toInt() }
                    } else {
                        val range = value.toString().toIntRange()
                        entities.filter { it.rotation.y() >= range!!.first && it.rotation.y() <= range.last }
                    }
                }
                "y_rotation" -> {
                    checkIntOrRange(value.toString())
                    entities = if (value.toString().startsWith("..")) {
                        val yaw = value.toString().replace("..", "").toInt()
                        entities.filter { it.rotation.x() <= yaw }
                    } else if (!value.toString().contains("..")) {
                        entities.filter { it.rotation.x().toInt() == value.toString().toInt() }
                    } else {
                        val range = value.toString().toIntRange()
                        entities.filter { it.rotation.x() >= range!!.first && it.rotation.x() <= range.last }
                    }
                }
                "type" -> notImplemented("type")
                "nbt" -> notImplemented("nbt")
                "advancements" -> notImplemented("advancements")
                "predicate" -> notImplemented("predicate")
                "sort" -> {
                    val sorter = EntityArguments.Sorter.fromName(value.toString())
                        ?: throw EntityArgumentExceptions.INVALID_SORT_TYPE.create(value)
                    entities = when (sorter) {
                        EntityArguments.Sorter.NEAREST -> entities.sortedBy { it.location.distanceSquared(source.location) }
                        EntityArguments.Sorter.FURTHEST -> entities.sortedByDescending { it.location.distanceSquared(source.location) }
                        EntityArguments.Sorter.RANDOM -> entities.shuffled()
                        EntityArguments.Sorter.ARBITRARY -> entities.sortedBy { it.ticksExisted }
                    }
                }
                "limit" -> {
                    checkInt(value.toString())
                    val limit = value.toString().toInt()
                    if (limit <= 0) throw EntityArgumentExceptions.LIMIT_NULL.create()
                    entities = if (entities.size > limit) entities.subList(0, limit - 1) else entities
                }
                else -> throw UnsupportedOperationException("Invalid option")
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
        throw DynamicCommandExceptionType { a -> text("Not yet implemented: $a").toMessage() }.create(option)
    }

    private fun checkInt(string: String) = string.toIntOrNull() ?: throw BrigadierExceptions.readerExpectedInt().create()

    private fun checkIntOrRange(string: String) {
        if (string.toIntOrNull() != null) return
        if (string.startsWith("..") && string.substring(2).toIntOrNull() != null) return
        if (string.contains("..") && string.toIntRange() != null) return
        throw SimpleCommandExceptionType(translatable("argument.range.empty").toMessage()).create()
    }

    enum class Selector {

        RANDOM_PLAYER,
        ALL_PLAYERS,
        EXECUTOR,
        ALL_ENTITIES,
        NEAREST_PLAYER,
        PLAYER,
        UNKNOWN;

        companion object {

            /**
             * Gets the target selector from it's short name
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

    companion object {

        private fun String.toIntRange(): IntRange? {
            if (startsWith("...")) {
                val string = replace("..", "").toIntOrNull() ?: return null
                return IntRange(0, string)
            }
            val values = split("..")
            return IntRange(values[0].toInt(), values[1].toIntOrNull() ?: return null)
        }
    }
}



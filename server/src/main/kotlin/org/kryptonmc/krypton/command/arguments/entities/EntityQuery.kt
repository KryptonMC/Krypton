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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.command.BrigadierExceptions
import org.kryptonmc.krypton.command.toExceptionType
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer

/**
 * Documentation [TODO]
 */
@JvmRecord
data class EntityQuery(
    private val args: List<EntityArgument.EntityArg>,
    val type: Selector,
    private val playerName: String = ""
) {

    fun entities(source: KryptonPlayer): List<KryptonEntity> = when (type) {
        Selector.RANDOM_PLAYER -> listOf(source.server.players.random())
        Selector.ALL_PLAYERS -> {
            if (args.isNotEmpty()) {
                val entities = applyArguments(source.server.players.asSequence().plus(source.world.entities), source)
                if (entities.isEmpty()) throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create()
                entities
            } else {
                source.server.players
            }
        }
        Selector.EXECUTOR -> listOf(source)
        Selector.ALL_ENTITIES -> {
            if (args.isNotEmpty()) {
                val entities = applyArguments(source.server.players.asSequence().plus(source.world.entities), source)
                if (entities.isEmpty()) throw EntityArgumentExceptions.ENTITY_NOT_FOUND.create()
                entities
            } else {
                source.server.players.plus(source.world.entities)
            }
        }
        Selector.NEAREST_PLAYER -> {
            var currentNearest = source.server.players[0]
            source.server.players.forEach {
                if (it.location.distanceSquared(source.location) < currentNearest.location.distanceSquared(source.location)) currentNearest = it
            }
            listOf(currentNearest)
        }
        Selector.PLAYER -> {
            val player = source.server.player(playerName) ?: throw EntityArgumentExceptions.UNKNOWN_PLAYER.create()
            listOf(player)
        }
        Selector.UNKNOWN -> throw EntityArgumentExceptions.UNKNOWN_SELECTOR.create("")
    }

    fun entity(source: KryptonPlayer): KryptonEntity = entities(source)[0]

    @Suppress("UNCHECKED_CAST")
    fun players(sender: Sender): List<KryptonPlayer> {
        val server = sender.server as KryptonServer
        if (sender is KryptonPlayer) {
            if (playerName.isNotEmpty()) {
                val player = server.player(playerName) ?: throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create()
                return listOf(player)
            }
            val entities = entities(sender)
            entities.forEach {
                if (it !is KryptonPlayer) throw UnsupportedOperationException("You cannot call players if there is an entity in the arguments!")
            }
            return entities as List<KryptonPlayer>
        }
        val player = server.player(playerName) ?: throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create()
        return listOf(player)
    }

    fun profiles(sender: Sender): List<KryptonGameProfile> {
        val server = sender.server as? KryptonServer ?: return emptyList()
        if (sender is KryptonPlayer) {
            if (playerName.isNotEmpty()) {
                val profile = server.profileCache[playerName] ?: throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create()
                return listOf(profile)
            }
            return players(sender).map { it.profile }
        }
        if (playerName.isNotEmpty()) {
            val profile = server.profileCache[playerName] ?: throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create()
            return listOf(profile)
        }
        return emptyList()
    }

    private fun applyArguments(originalEntities: Sequence<KryptonEntity>, source: KryptonPlayer): List<KryptonEntity> {
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
                    if (value.toString().startsWith("..")) {
                        val distance = value.toString().replace("..", "").toInt()
                        entities = entities.filter { it.location.distanceSquared(source.location) <= distance }
                        continue
                    }
                    if (!value.toString().contains("..")) {
                        checkInt(value.toString())
                        entities = entities.filter {
                            val int = it.location.distanceSquared(source.location).toInt()
                            if (int < 0) throw EntityArgumentExceptions.DISTANCE_NEGATIVE.create()
                            int == value.toString().toInt()
                        }
                        continue
                    }
                    val range = value.toString().toIntRange()!!
                    entities = entities.filter {
                        it.location.distanceSquared(source.location) >= range.first && it.location.distanceSquared(source.location) <= range.last
                    }
                }
                "scores" -> notImplemented("scores")
                "tag" -> notImplemented("tag")
                "team" -> notImplemented("team")
                "level" -> notImplemented("level")
                "gamemode" -> {
                    entities = entities.filter {
                        if (it !is KryptonPlayer) return@filter true
                        val mode = GameMode.fromName(value.toString())
                        if (exclude) it.gameMode != mode else it.gameMode == mode
                    }
                }
                "name" -> {
                    entities = entities.filter {
                        val name = if (it is Player) it.profile.name else it.name.toPlainText()
                        if (exclude) name != value else name == value
                    }
                }
                "x_rotation" -> {
                    checkIntOrRange(value.toString())
                    if (value.toString().startsWith("..")) {
                        val pitch = value.toString().replace("..", "").toInt()
                        entities = entities.filter { it.rotation.y() <= pitch }
                        continue
                    }
                    if (!value.toString().contains("..")) {
                        entities = entities.filter { it.rotation.y().toInt() == value.toString().toInt() }
                        continue
                    }
                    val range = value.toString().toIntRange()!!
                    entities = entities.filter { it.rotation.y() >= range.first && it.rotation.y() <= range.last }
                }
                "y_rotation" -> {
                    checkIntOrRange(value.toString())
                    if (value.toString().startsWith("..")) {
                        val yaw = value.toString().replace("..", "").toInt()
                        entities = entities.filter { it.rotation.x() <= yaw }
                        continue
                    }
                    if (!value.toString().contains("..")) {
                        entities = entities.filter { it.rotation.x().toInt() == value.toString().toInt() }
                        continue
                    }
                    val range = value.toString().toIntRange()!!
                    entities = entities.filter { it.rotation.x() >= range.first && it.rotation.x() <= range.last }
                }
                "type" -> notImplemented("type")
                "nbt" -> notImplemented("nbt")
                "advancements" -> notImplemented("advancements")
                "predicate" -> notImplemented("predicate")
                "sort" -> {
                    val sorter = EntityArguments.Sorter.fromName(value.toString()) ?: throw EntityArgumentExceptions.INVALID_SORT_TYPE.create(value)
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
                    if (limit <= 0) throw EntityArgumentExceptions.LIMIT_NEGATIVE.create()
                    entities = if (entities.count() > limit) entities.take(limit - 1) else entities
                }
                else -> throw UnsupportedOperationException("Invalid option")
            }
        }
        return entities.toList()
    }

    private fun applyDifference(difference: Int, value: Any, blockCoordinate: Int): Boolean {
        if (difference != 0) {
            val min = value.toString().toInt()
            val max = min + difference
            return blockCoordinate in min..max
        }
        return blockCoordinate == value.toString().toInt()
    }

    private fun notImplemented(option: String) {
        throw NOT_IMPLEMENTED.create(option)
    }

    private fun checkInt(string: String) {
        if (string.toIntOrNull() == null) throw BrigadierExceptions.readerExpectedInt().create()
    }

    private fun checkIntOrRange(string: String) {
        if (string.toIntOrNull() != null) return
        if (string.startsWith("..") && string.substring(2).toIntOrNull() != null) return
        if (string.contains("..") && string.toIntRange() != null) return
        throw OUT_OF_RANGE.create()
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

            const val RANDOM_PLAYER_CHAR: Char = 'r'
            const val ALL_PLAYERS_CHAR: Char = 'a'
            const val EXECUTOR_CHAR: Char = 's'
            const val ALL_ENTITIES_CHAR: Char = 'e'
            const val NEAREST_PLAYER_CHAR: Char = 'p'

            /**
             * Gets the target selector from it's short name
             * you can find these at the [Minecraft Wiki](https://minecraft.fandom.com/wiki/Target_selectors)
             */
            @JvmStatic
            fun fromChar(selector: Char): Selector = when (selector) {
                RANDOM_PLAYER_CHAR -> RANDOM_PLAYER
                ALL_PLAYERS_CHAR -> ALL_PLAYERS
                EXECUTOR_CHAR -> EXECUTOR
                ALL_ENTITIES_CHAR -> ALL_ENTITIES
                NEAREST_PLAYER_CHAR -> NEAREST_PLAYER
                else -> UNKNOWN
            }
        }
    }

    companion object {

        private val NOT_IMPLEMENTED = DynamicCommandExceptionType { Component.text(it.toString()).toMessage() }
        private val OUT_OF_RANGE = Component.translatable("argument.range.empty").toExceptionType()

        @JvmStatic
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

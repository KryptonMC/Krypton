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
package org.kryptonmc.krypton.command.arguments.entities

import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.krypton.adventure.KryptonAdventure
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.collection.DowncastingList
import org.kryptonmc.krypton.util.enumhelper.GameModes

@JvmRecord
data class EntityQuery(val type: Selector, private val args: List<EntityArgument>, private val playerName: String) {

    constructor(type: Selector, args: List<EntityArgument>) : this(type, args, "")

    constructor(type: Selector, playerName: String) : this(type, emptyList(), playerName)

    constructor(type: Selector) : this(type, emptyList(), "")

    fun getEntities(source: KryptonPlayer): List<KryptonEntity> {
        val players = source.server.playerManager.players()
        when (type) {
            Selector.RANDOM_PLAYER -> return listOf(players.random())
            Selector.ALL_PLAYERS -> {
                if (args.isNotEmpty()) return tryApplyEntities(source, EntityArgumentExceptions.PLAYER_NOT_FOUND)
                return players
            }
            Selector.EXECUTOR -> return listOf(source)
            Selector.ALL_ENTITIES -> {
                if (args.isNotEmpty()) return tryApplyEntities(source, EntityArgumentExceptions.ENTITY_NOT_FOUND)
                return players.plus(source.world.entities)
            }
            Selector.NEAREST_PLAYER -> {
                var currentNearest = players.get(0)
                players.forEach { if (distance(it, source) < distance(currentNearest, source)) currentNearest = it }
                return listOf(currentNearest)
            }
            Selector.PLAYER -> {
                val player = source.server.getPlayer(playerName) ?: throw EntityArgumentExceptions.UNKNOWN_PLAYER.create()
                return listOf(player)
            }
            Selector.UNKNOWN -> throw EntityArgumentExceptions.UNKNOWN_SELECTOR.create("")
        }
    }

    private fun tryApplyEntities(source: KryptonPlayer, exceptionType: SimpleCommandExceptionType): List<KryptonEntity> {
        val entities = applyArguments(source.server.players.asSequence().plus(source.world.entities), source)
        if (entities.isEmpty()) throw exceptionType.create()
        return entities
    }

    fun getPlayers(source: CommandSourceStack): List<KryptonPlayer> {
        if (source.isPlayer()) {
            if (playerName.isNotEmpty()) return listOf(playerOrThrow(source.server.getPlayer(playerName)))
            return EntityAsPlayerList(getEntities(source.entity as KryptonPlayer))
        }
        return listOf(playerOrThrow(source.server.getPlayer(playerName)))
    }

    fun getProfiles(source: CommandSourceStack): List<GameProfile> {
        if (source.isPlayer()) {
            if (playerName.isNotEmpty()) return listOf(playerOrThrow(source.server.profileCache.getProfile(playerName)))
            return getPlayers(source).map { it.profile }
        }
        if (playerName.isNotEmpty()) return listOf(playerOrThrow(source.server.profileCache.getProfile(playerName)))
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
                    entities = entities.filter { applyDifference(differenceX, value, it.position.blockX()) }
                }
                "y" -> {
                    checkInt(value.toString())
                    entities = entities.filter { applyDifference(differenceY, value, it.position.blockY()) }
                }
                "z" -> {
                    checkInt(value.toString())
                    entities = entities.filter { applyDifference(differenceZ, value, it.position.blockZ()) }
                }
                "distance" -> entities = applyDifferenceArgument(source, entities, value)
                "scores" -> notImplemented("scores")
                "tag" -> notImplemented("tag")
                "team" -> notImplemented("team")
                "level" -> notImplemented("level")
                "gamemode" -> entities = applyGameModeArgument(entities, value, exclude)
                "name" -> entities = applyNameArgument(entities, value, exclude)
                "x_rotation" -> entities = applyRotationArgument(entities, value) { it.position.pitch }
                "y_rotation" -> entities = applyRotationArgument(entities, value) { it.position.yaw }
                "type" -> notImplemented("type")
                "nbt" -> notImplemented("nbt")
                "advancements" -> notImplemented("advancements")
                "predicate" -> notImplemented("predicate")
                "sort" -> {
                    val sorter = EntityArguments.Sorter.fromName(value.toString()) ?: throw EntityArgumentExceptions.INVALID_SORT_TYPE.create(value)
                    entities = when (sorter) {
                        EntityArguments.Sorter.NEAREST -> entities.sortedBy { distance(it, source) }
                        EntityArguments.Sorter.FURTHEST -> entities.sortedByDescending { distance(it, source) }
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

    private fun applyGameModeArgument(entities: Sequence<KryptonEntity>, value: Any, exclude: Boolean): Sequence<KryptonEntity> = entities.filter {
        if (it !is KryptonPlayer) return@filter true
        val mode = GameModes.fromName(value.toString())
        if (exclude) it.gameMode != mode else it.gameMode == mode
    }

    private fun applyNameArgument(entities: Sequence<KryptonEntity>, value: Any, exclude: Boolean): Sequence<KryptonEntity> = entities.filter {
        val name = it.name
        if (exclude) name != value else name == value
    }

    private fun applyDifferenceArgument(source: KryptonPlayer, entities: Sequence<KryptonEntity>, value: Any): Sequence<KryptonEntity> {
        checkIntOrRange(value.toString())
        if (value.toString().startsWith("..")) {
            val distance = value.toString().replace("..", "").toInt()
            return entities.filter { distance(it, source) <= distance }
        }
        if (!value.toString().contains("..")) {
            checkInt(value.toString())
            return entities.filter {
                val distance = distance(it, source).toInt()
                if (distance < 0) throw EntityArgumentExceptions.DISTANCE_NEGATIVE.create()
                distance == value.toString().toInt()
            }
        }
        val range = toIntRange(value.toString())!!
        return entities.filter {
            val distance = distance(it, source)
            distance >= range.first && distance <= range.last
        }
    }

    private inline fun applyRotationArgument(entities: Sequence<KryptonEntity>, value: Any,
                                             crossinline valueGetter: (KryptonEntity) -> Float): Sequence<KryptonEntity> {
        checkIntOrRange(value.toString())
        if (value.toString().startsWith("..")) {
            val pitch = value.toString().replace("..", "").toInt()
            return entities.filter { valueGetter(it) <= pitch }
        }
        if (!value.toString().contains("..")) return entities.filter { valueGetter(it).toInt() == value.toString().toInt() }
        val range = toIntRange(value.toString())!!
        return entities.filter { valueGetter(it) >= range.first && valueGetter(it) <= range.last }
    }

    private fun applyDifference(difference: Int, value: Any, blockCoordinate: Int): Boolean {
        if (difference != 0) {
            val min = value.toString().toInt()
            return blockCoordinate >= min && blockCoordinate <= min + difference
        }
        return blockCoordinate == value.toString().toInt()
    }

    private fun notImplemented(option: String) {
        throw NOT_IMPLEMENTED.create(option)
    }

    private fun checkInt(string: String) {
        if (string.toIntOrNull() == null) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().create()
    }

    private fun checkIntOrRange(string: String) {
        if (string.toIntOrNull() != null) return
        if (string.startsWith("..") && string.substring(2).toIntOrNull() != null) return
        if (string.contains("..") && toIntRange(string) != null) return
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
        }
    }

    private class EntityAsPlayerList(delegate: List<KryptonEntity>) : DowncastingList<KryptonEntity, KryptonPlayer>(delegate) {

        override fun createThis(delegate: List<KryptonEntity>): DowncastingList<KryptonEntity, KryptonPlayer> = EntityAsPlayerList(delegate)

        override fun downcast(element: KryptonEntity): KryptonPlayer {
            if (element !is KryptonPlayer) throw UnsupportedOperationException("Cannot call players if there is an entity in the arguments!")
            return element
        }
    }

    companion object {

        private val NOT_IMPLEMENTED = DynamicCommandExceptionType { KryptonAdventure.asMessage(Component.text(it.toString())) }
        private val OUT_OF_RANGE = CommandExceptions.simple("argument.range.empty")

        @JvmStatic
        private fun toIntRange(input: String): IntRange? {
            if (input.startsWith("...")) {
                try {
                    return IntRange(0, input.replace("...", "").toInt())
                } catch (_: NumberFormatException) {
                    return null
                }
            }
            val values = input.split("..")
            try {
                return IntRange(values.get(0).toInt(), values.get(1).toInt())
            } catch (_: NumberFormatException) {
                return null
            }
        }

        @JvmStatic
        private fun distance(first: KryptonEntity, second: KryptonEntity): Double = first.position.distanceSquared(second.position)

        @JvmStatic
        private fun <T : Any> playerOrThrow(player: T?): T = player ?: throw EntityArgumentExceptions.PLAYER_NOT_FOUND.create()
    }
}

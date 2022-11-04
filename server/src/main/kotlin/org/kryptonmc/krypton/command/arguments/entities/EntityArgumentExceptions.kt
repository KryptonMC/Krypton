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

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.adventure.toMessage
import org.kryptonmc.krypton.command.toExceptionType

/**
 * Various exceptions that may be thrown as a result of trying to parse
 * entity selectors.
 */
object EntityArgumentExceptions {

    /**
     * Thrown when a user fails to actually input a selector. This is the case
     * when the user only inputs the [EntityArgumentParser.SELECTOR_CHAR], and
     * not another character indicating what the user actually wants to select.
     */
    @JvmField
    val MISSING_SELECTOR: SimpleCommandExceptionType = Component.translatable("argument.entity.selector.missing").toExceptionType()

    /**
     * Thrown when a user inputs a selector that is not recognised by the
     * parser as a valid selector.
     *
     * For example, the following are invalid and will throw this exception:
     * - `@t`
     * - `@u`
     * - `@v`
     */
    @JvmField
    val UNKNOWN_SELECTOR: DynamicCommandExceptionType = DynamicCommandExceptionType {
        Component.translatable("argument.entity.selector.unknown", Component.text(it.toString())).toMessage()
    }

    /**
     * Thrown when a user inputs a selector filter and doesn't terminate the
     * selection with [EntityArgumentParser.CLOSING_BRACKET].
     *
     * For example, the following are invalid and will throw this exception:
     * - `@e[type=!player]`
     * - `@e[dx=12`
     * - `@e[dz=74`
     */
    @JvmField
    val UNTERMINATED: SimpleCommandExceptionType = Component.translatable("argument.entity.options.unterminated").toExceptionType()

    /**
     * Thrown when a user fails to input a value for a selector filter.
     *
     * For example, the following are invalid and will throw this exception:
     * - `@e[type=`
     * - `@e[dx=`
     * - `@e[dz=`
     */
    @JvmField
    val VALUELESS: DynamicCommandExceptionType = DynamicCommandExceptionType {
        Component.translatable("argument.entity.options.valueless", Component.text(it.toString())).toMessage()
    }

    /**
     * Thrown when a user inputs an invalid selector filter.
     *
     * For example, the following are invalid and will throw this exception:
     * - `@e[joe=bloggs]`
     * - `@e[a=c]`
     * - `@e[hello=world]`
     */
    @JvmField
    val INVALID_OPTION: DynamicCommandExceptionType = DynamicCommandExceptionType {
        Component.translatable("argument.entity.options.unknown", Component.text(it.toString())).toMessage()
    }

    /**
     * Thrown when a user tries to select a player that could not be found,
     * meaning they are not on this server.
     */
    @JvmField
    val PLAYER_NOT_FOUND: SimpleCommandExceptionType = Component.translatable("argument.entity.notfound.player").toExceptionType()

    /**
     * Thrown when a user tries to select an entity that could not be found,
     * meaning it does not exist on the server.
     */
    @JvmField
    val ENTITY_NOT_FOUND: SimpleCommandExceptionType = Component.translatable("argument.entity.notfound.entity").toExceptionType()

    /**
     * Thrown when a user tries to select more than one entity when the parser
     * required that a single entity be selected.
     */
    @JvmField
    val TOO_MANY_ENTITIES: SimpleCommandExceptionType = Component.translatable("argument.entity.toomany").toExceptionType()

    /**
     * Thrown when a user tries to select more than one player when the parser
     * required that a single player be selected.
     */
    @JvmField
    val TOO_MANY_PLAYERS: SimpleCommandExceptionType = Component.translatable("argument.player.toomany").toExceptionType()

    /**
     * Thrown when a user tries to select entities and the parser requires
     * that only players be selected.
     */
    @JvmField
    val ONLY_FOR_PLAYERS: SimpleCommandExceptionType = Component.translatable("argument.player.entities").toExceptionType()

    /**
     * Thrown when a player selector is parsed and the name does not resolve
     * to an online player.
     */
    @JvmField
    val UNKNOWN_PLAYER: SimpleCommandExceptionType = Component.translatable("argument.player.unknown").toExceptionType()

    /**
     * Thrown when a user inputs a value for a limit selector filter that is
     * less than 0.
     */
    @JvmField
    val LIMIT_NEGATIVE: SimpleCommandExceptionType = Component.translatable("argument.entity.options.limit.toosmall").toExceptionType()

    /**
     * Thrown when a user inputs a value for a distance selector filter that
     * is less than 0.
     */
    @JvmField
    val DISTANCE_NEGATIVE: SimpleCommandExceptionType = Component.translatable("argument.entity.options.distance.negative").toExceptionType()

    /**
     * Thrown when a user inputs an invalid sort type for a sorting selector
     * filter.
     */
    @JvmField
    val INVALID_SORT_TYPE: DynamicCommandExceptionType = DynamicCommandExceptionType {
        Component.translatable("argument.entity.options.sort.irreversible", Component.text(it.toString())).toMessage()
    }
}

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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toMessage

object EntityArgumentExceptions {

    @JvmField
    val MISSING_SELECTOR: SimpleCommandExceptionType = SimpleCommandExceptionType(
        Component.translatable("argument.entity.selector.missing").toMessage()
    )

    @JvmField
    val UNKNOWN_SELECTOR: DynamicCommandExceptionType = DynamicCommandExceptionType {
        Component.translatable("argument.entity.selector.unknown", Component.text(it.toString())).toMessage()
    }

    @JvmField
    val UNTERMINATED: SimpleCommandExceptionType = SimpleCommandExceptionType(
        Component.translatable("argument.entity.options.unterminated").toMessage()
    )

    @JvmField
    val VALUELESS: DynamicCommandExceptionType = DynamicCommandExceptionType {
        Component.translatable("argument.entity.options.valueless", Component.text(it.toString())).toMessage()
    }

    @JvmField
    val INVALID_OPTION: DynamicCommandExceptionType = DynamicCommandExceptionType {
        Component.translatable("argument.entity.options.unknown", Component.text(it.toString())).toMessage()
    }

    @JvmField
    val PLAYER_NOT_FOUND: SimpleCommandExceptionType = SimpleCommandExceptionType(
        Component.translatable("argument.entity.notfound.player").toMessage()
    )

    @JvmField
    val ENTITY_NOT_FOUND: SimpleCommandExceptionType = SimpleCommandExceptionType(
        Component.translatable("argument.entity.notfound.entity").toMessage()
    )

    @JvmField
    val TOO_MANY_ENTITIES: SimpleCommandExceptionType = SimpleCommandExceptionType(Component.translatable("argument.entity.toomany").toMessage())

    @JvmField
    val TOO_MANY_PLAYERS: SimpleCommandExceptionType = SimpleCommandExceptionType(Component.translatable("argument.player.toomany").toMessage())

    @JvmField
    val ONLY_FOR_PLAYERS: SimpleCommandExceptionType = SimpleCommandExceptionType(Component.translatable("argument.player.entities").toMessage())

    @JvmField
    val UNKNOWN_PLAYER: SimpleCommandExceptionType = SimpleCommandExceptionType(Component.translatable("argument.player.unknown").toMessage())

    @JvmField
    val LIMIT_NULL: SimpleCommandExceptionType = SimpleCommandExceptionType(
        Component.translatable("argument.entity.options.limit.toosmall").toMessage()
    )

    @JvmField
    val DISTANCE_NEGATIVE: SimpleCommandExceptionType = SimpleCommandExceptionType(
        Component.translatable("argument.entity.options.distance.negative").toMessage()
    )

    @JvmField
    val INVALID_SORT_TYPE: DynamicCommandExceptionType = DynamicCommandExceptionType {
        Component.translatable("argument.entity.options.sort.irreversible", Component.text(it.toString())).toMessage()
    }
}

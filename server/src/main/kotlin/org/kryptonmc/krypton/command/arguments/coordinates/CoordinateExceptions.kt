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
package org.kryptonmc.krypton.command.arguments.coordinates

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.command.toExceptionType

/**
 * Various exceptions that may be thrown as a result of trying to parse
 * coordinates.
 */
object CoordinateExceptions {

    /**
     * Thrown when a user fails to input a value for a coordinate.
     *
     * This is only used by local coordinates, as they are the only coordinates
     * that require values for all 3 coordinates to be inputted.
     *
     * For example, the following are invalid and will throw this exception:
     * - `^ ^ ^`
     * - `^3 ^ ^4`
     * - `^6 ^2 ^`
     */
    @JvmField
    val POSITION_EXPECTED_DOUBLE: SimpleCommandExceptionType = Component.translatable("argument.pos.missing.double").toExceptionType()

    /**
     * Thrown when a user fails to input all 3 coordinates that are required.
     *
     * For example, the following are invalid and will throw this exception:
     * - `^3 ^2`
     * - `~4 ~`
     * - `6 4`
     */
    @JvmField
    val POSITION_3D_INCOMPLETE: SimpleCommandExceptionType = Component.translatable("argument.pos3d.incomplete").toExceptionType()

    /**
     * Thrown when a user inputs coordinates of different types in to the same
     * string.
     *
     * For example, the following are invalid and will throw this exception:
     * - `^3 ~4 8`
     * - `4 ~2 9`
     * - `~6 ^4 0`
     */
    @JvmField
    val POSITION_MIXED_TYPE: SimpleCommandExceptionType = Component.translatable("argument.pos.mixed").toExceptionType()
}

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
package org.kryptonmc.krypton.shapes

fun interface BooleanOperator {

    fun apply(left: Boolean, right: Boolean): Boolean

    companion object {

        @JvmField
        val TRUE: BooleanOperator = BooleanOperator { _, _ -> true }
        @JvmField
        val FALSE: BooleanOperator = BooleanOperator { _, _ -> false }
        @JvmField
        val FIRST: BooleanOperator = BooleanOperator { left, _ -> left }
        @JvmField
        val SECOND: BooleanOperator = BooleanOperator { _, right -> right }
        @JvmField
        val NOT_FIRST: BooleanOperator = BooleanOperator { left, _ -> !left }
        @JvmField
        val NOT_SECOND: BooleanOperator = BooleanOperator { _, right -> !right }
        @JvmField
        val ONLY_FIRST: BooleanOperator = BooleanOperator { left, right -> left && !right }
        @JvmField
        val ONLY_SECOND: BooleanOperator = BooleanOperator { left, right -> !left && right }
        @JvmField
        val AND: BooleanOperator = BooleanOperator { left, right -> left && right }
        @JvmField
        val OR: BooleanOperator = BooleanOperator { left, right -> left || right }
        @JvmField
        val NOT_AND: BooleanOperator = BooleanOperator { left, right -> !left || !right }
        @JvmField
        val NOT_OR: BooleanOperator = BooleanOperator { left, right -> !left && !right }
        @JvmField
        val SAME: BooleanOperator = BooleanOperator { left, right -> left == right }
        @JvmField
        val NOT_SAME: BooleanOperator = BooleanOperator { left, right -> left != right }
        @JvmField
        val CAUSES: BooleanOperator = BooleanOperator { left, right -> !left || right }
        @JvmField
        val CAUSED_BY: BooleanOperator = BooleanOperator { left, right -> left || !right }
    }
}

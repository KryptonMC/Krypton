/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.shapes.util

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

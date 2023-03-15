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
package org.kryptonmc.krypton.util

import kotlin.math.ceil

object ByteBufExtras {

    private val VARINT_EXACT_BYTE_LENGTHS = IntArray(33) { ceil((31.0 - (it - 1)) / 7.0).toInt() }.apply { this[32] = 1 }

    fun getVarIntBytes(value: Int): Int = VARINT_EXACT_BYTE_LENGTHS[value.countLeadingZeroBits()]

    @JvmStatic
    inline fun <T> limitValue(crossinline function: (Int) -> T, limit: Int): (Int) -> T = {
        if (it > limit) error("Value $it is larger than limit $limit!")
        function(it)
    }
}

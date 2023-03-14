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
package org.kryptonmc.krypton.command.arguments.coordinates

import com.mojang.brigadier.StringReader
import org.kryptonmc.krypton.command.arguments.StringReading

@JvmRecord
data class WorldCoordinate(val value: Double, val isRelative: Boolean) {

    fun get(relativeTo: Double): Double = if (isRelative) value + relativeTo else value

    companion object {

        @JvmStatic
        fun parse(reader: StringReader, correctCenter: Boolean): WorldCoordinate {
            // Got a caret (local coordinate) when we expected to get relative coordinates
            if (reader.canRead() && reader.peek() == TextCoordinates.LOCAL_MODIFIER) {
                throw CoordinateExceptions.POSITION_MIXED_TYPE.createWithContext(reader)
            }
            if (!reader.canRead()) throw CoordinateExceptions.POSITION_EXPECTED_DOUBLE.createWithContext(reader)

            // Check for relative positioning
            val relative = reader.peek() == TextCoordinates.RELATIVE_MODIFIER
            if (relative) reader.skip()

            val position = reader.cursor
            var value = StringReading.readOptionalDouble(reader)
            val remaining = reader.string.substring(position, reader.cursor)
            if (relative && remaining.isEmpty()) return WorldCoordinate(0.0, true)
            if (!remaining.contains('.') && !relative && correctCenter) value += 0.5
            return WorldCoordinate(value, relative)
        }
    }
}

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
import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.command.CommandSourceStack

@JvmRecord
data class WorldCoordinates(val x: WorldCoordinate, val y: WorldCoordinate, val z: WorldCoordinate) : Coordinates {

    override fun calculatePosition(source: CommandSourceStack): Position {
        return Position(x.get(source.position.x), y.get(source.position.y), z.get(source.position.z))
    }

    companion object {

        @JvmStatic
        fun parse(reader: StringReader, correctCenter: Boolean): WorldCoordinates {
            val position = reader.cursor
            val x = WorldCoordinate.parse(reader, correctCenter)
            CoordinateExceptions.checkPositionComplete(reader, position)
            val y = WorldCoordinate.parse(reader, false)
            CoordinateExceptions.checkPositionComplete(reader, position)
            val z = WorldCoordinate.parse(reader, correctCenter)
            return WorldCoordinates(x, y, z)
        }
    }
}

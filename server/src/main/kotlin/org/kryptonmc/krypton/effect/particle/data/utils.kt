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
package org.kryptonmc.krypton.effect.particle.data

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.util.Color

// This constant is used to transform an integer colour value (between 0 and 255 because it's RGB) to a floating point value (between 0.0 and 1.0)
// and vice versa.
private const val COLOR_ENCODING_VALUE = 255F

internal fun ByteBuf.readParticleColor(): Color = Color(readColorValue(), readColorValue(), readColorValue())

private fun ByteBuf.readColorValue(): Int = (readFloat() * COLOR_ENCODING_VALUE).toInt()

internal fun ByteBuf.writeParticleColor(color: Color) {
    writeFloat(if (color.red == 0) Float.MIN_VALUE else color.red.toFloat() / COLOR_ENCODING_VALUE)
    writeFloat(color.green.toFloat() / COLOR_ENCODING_VALUE)
    writeFloat(color.blue.toFloat() / COLOR_ENCODING_VALUE)
}

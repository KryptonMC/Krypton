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
package org.kryptonmc.krypton.adventure.network

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.kryptonmc.krypton.util.resultOrError
import org.kryptonmc.serialization.DataResult
import java.util.Locale

object TextColorSerialization {

    @JvmStatic
    fun encode(color: TextColor): String {
        if (color is NamedTextColor) return NamedTextColor.NAMES.key(color)!!
        return String.format(Locale.ROOT, "#%06X", color.value())
    }

    @JvmStatic
    fun decode(input: String): TextColor? = if (input.startsWith("#")) TextColor.fromHexString(input) else NamedTextColor.NAMES.value(input)

    @JvmStatic
    fun decodeResult(input: String): DataResult<TextColor> =
        decode(input).resultOrError { "Input string $input is not a valid named colour or hex colour!" }
}

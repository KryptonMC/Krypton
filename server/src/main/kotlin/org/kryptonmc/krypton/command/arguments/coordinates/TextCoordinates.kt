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

@JvmRecord
data class TextCoordinates(val x: Char, val y: Char, val z: Char) {

    companion object {

        const val LOCAL_MODIFIER: Char = '^'
        const val RELATIVE_MODIFIER: Char = '~'

        @JvmField
        val CENTER_LOCAL: TextCoordinates = TextCoordinates(LOCAL_MODIFIER, LOCAL_MODIFIER, LOCAL_MODIFIER)
        @JvmField
        val CENTER_GLOBAL: TextCoordinates = TextCoordinates(RELATIVE_MODIFIER, RELATIVE_MODIFIER, RELATIVE_MODIFIER)
    }
}

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
package org.kryptonmc.krypton.pack.repository

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.pack.PackType

enum class PackCompatibility(type: String) {

    TOO_OLD("old"),
    TOO_NEW("new"),
    COMPATIBLE("compatible");

    private val description = Component.translatable("pack.incompatible.$type", NamedTextColor.GRAY)
    private val confirmation = Component.translatable("pack.incompatible.confirm.$type")

    fun description(): Component = description

    fun confirmation(): Component = confirmation

    fun isCompatible(): Boolean = this == COMPATIBLE

    companion object {

        @JvmStatic
        fun forFormat(format: Int, type: PackType): PackCompatibility {
            val version = type.version()
            return when {
                format < version -> TOO_OLD
                format > version -> TOO_NEW
                else -> COMPATIBLE
            }
        }
    }
}

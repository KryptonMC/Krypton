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
package org.kryptonmc.krypton.pack.repository

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.pack.metadata.PackMetadata

enum class PackCompatibility(type: String) {

    TOO_OLD("old"),
    TOO_NEW("new"),
    COMPATIBLE("compatible");

    val description: Component = Component.translatable("pack.incompatible.$type", NamedTextColor.GRAY)
    val confirmation: Component = Component.translatable("pack.incompatible.confirm.$type")

    val isCompatible: Boolean
        get() = this == COMPATIBLE

    companion object {

        @JvmStatic
        fun forMetadata(metadata: PackMetadata): PackCompatibility {
            if (metadata.format < KryptonPlatform.dataPackVersion) return TOO_OLD
            if (metadata.format > KryptonPlatform.dataPackVersion) return TOO_NEW
            return COMPATIBLE
        }
    }
}

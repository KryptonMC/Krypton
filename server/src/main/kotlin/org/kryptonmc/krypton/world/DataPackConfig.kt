/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.world

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.CompoundEncoder
import org.kryptonmc.krypton.util.serialization.encode
import org.kryptonmc.nbt.compound

@JvmRecord
data class DataPackConfig(val enabled: ImmutableList<String>, val disabled: ImmutableList<String>) {

    companion object {

        @JvmField
        val DEFAULT: DataPackConfig = DataPackConfig(persistentListOf("vanilla"), persistentListOf())

        @JvmField
        val ENCODER: CompoundEncoder<DataPackConfig> = CompoundEncoder {
            compound {
                encode(Codecs.STRING_LIST, "Enabled", it.enabled)
                encode(Codecs.STRING_LIST, "Disabled", it.disabled)
            }
        }
    }
}

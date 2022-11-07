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
package org.kryptonmc.krypton.effect.sound

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.sound.SoundEvent

@JvmRecord
data class KryptonSoundEvent(private val key: Key, override val range: Float) : SoundEvent {

    override fun key(): Key = key

    companion object {

        // This is the default range that sounds travel (from vanilla) that the majority of sounds have from before ranged sounds were added.
        const val DEFAULT_RANGE: Float = 16F
    }
}

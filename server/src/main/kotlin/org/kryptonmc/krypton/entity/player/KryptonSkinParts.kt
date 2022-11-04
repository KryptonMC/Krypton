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
package org.kryptonmc.krypton.entity.player

import org.kryptonmc.api.entity.player.SkinParts

@JvmRecord
data class KryptonSkinParts(private val raw: Int) : SkinParts {

    override val hasCape: Boolean
        get() = raw and FLAG_CAPE != 0
    override val hasJacket: Boolean
        get() = raw and FLAG_JACKET != 0
    override val hasLeftSleeve: Boolean
        get() = raw and FLAG_LEFT_SLEEVE != 0
    override val hasRightSleeve: Boolean
        get() = raw and FLAG_RIGHT_SLEEVE != 0
    override val hasLeftPants: Boolean
        get() = raw and FLAG_LEFT_PANTS != 0
    override val hasRightPants: Boolean
        get() = raw and FLAG_RIGHT_PANTS != 0
    override val hasHat: Boolean
        get() = raw and FLAG_HAT != 0

    companion object {

        private const val FLAG_CAPE = 0x01
        private const val FLAG_JACKET = 0x02
        private const val FLAG_LEFT_SLEEVE = 0x04
        private const val FLAG_RIGHT_SLEEVE = 0x08
        private const val FLAG_LEFT_PANTS = 0x10
        private const val FLAG_RIGHT_PANTS = 0x20
        private const val FLAG_HAT = 0x40

        @JvmField
        val ALL: SkinParts = KryptonSkinParts(127)
    }
}

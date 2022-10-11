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

import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.player.ChatVisibility
import org.kryptonmc.api.entity.player.PlayerSettings
import org.kryptonmc.api.entity.player.SkinParts
import java.util.Locale

@JvmRecord
data class KryptonPlayerSettings(
    override val locale: Locale?,
    override val viewDistance: Int,
    override val chatVisibility: ChatVisibility,
    override val hasChatColors: Boolean,
    override val skinParts: SkinParts,
    override val mainHand: MainHand,
    override val allowsServerListing: Boolean
) : PlayerSettings {

    companion object {

        @JvmField
        val DEFAULT: PlayerSettings = KryptonPlayerSettings(null, 10, ChatVisibility.FULL, true, KryptonSkinParts.ALL, MainHand.RIGHT, true)
    }
}

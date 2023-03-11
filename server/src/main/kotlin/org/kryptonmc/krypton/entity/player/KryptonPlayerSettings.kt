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
    val filterText: Boolean,
    override val allowsServerListing: Boolean
) : PlayerSettings {

    companion object {

        @JvmField
        val DEFAULT: KryptonPlayerSettings =
            KryptonPlayerSettings(null, 10, ChatVisibility.FULL, true, KryptonSkinParts.ALL, MainHand.RIGHT, false, true)
    }
}

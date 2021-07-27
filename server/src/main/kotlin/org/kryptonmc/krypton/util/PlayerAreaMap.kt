/*
 * This file is part of the Krypton project, and originates from the Paper project.
 *
 * The patch this originates from is licensed under the terms of the MIT license,
 * and the Kotlin translation of it is now licensed under the terms of the GNU
 * General Public License v3.0.
 *
 * Copyright (C) PaperMC
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
package org.kryptonmc.krypton.util

import org.kryptonmc.krypton.entity.player.KryptonPlayer

class PlayerAreaMap(
    pooledLinkedHashSets: PooledLinkedHashSets<KryptonPlayer> = PooledLinkedHashSets(),
    addCallback: ChangeCallback<KryptonPlayer>? = null,
    removeCallback: ChangeCallback<KryptonPlayer>? = null,
    changeSourceCallback: ChangeSourceCallback<KryptonPlayer>? = null
) : AreaMap<KryptonPlayer>(pooledLinkedHashSets, addCallback, removeCallback, changeSourceCallback) {

    override fun emptySetFor(element: KryptonPlayer) = element.cachedSingleHashSet
}

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
package org.kryptonmc.krypton.world.block.state

import com.google.common.collect.ImmutableMap
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec

class KryptonBlockState(
    owner: KryptonBlock,
    values: ImmutableMap<KryptonProperty<*>, Comparable<*>>,
    propertiesCodec: MapCodec<KryptonBlockState>
) : BlockBehaviour.BlockStateBase(owner, values, propertiesCodec) {

    override fun asState(): KryptonBlockState = this

    companion object {

        // FIXME: Don't cast this when we update the way we do registries.
        @JvmField
        @Suppress("UNCHECKED_CAST")
        val CODEC: Codec<KryptonBlockState> =
            codec(KryptonRegistries.BLOCK.byNameCodec() as Codec<KryptonBlock>, KryptonBlock::defaultState).stable()
    }
}

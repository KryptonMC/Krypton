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
package org.kryptonmc.krypton.world.dimension

import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import java.util.function.Function

@JvmRecord
data class WorldDimension(val type: Holder<KryptonDimensionType>) {

    companion object {

        @JvmField
        val OVERWORLD: ResourceKey<WorldDimension> = KryptonResourceKey.of(KryptonResourceKeys.DIMENSION, Key.key("overworld"))
        @JvmField
        val NETHER: ResourceKey<WorldDimension> = KryptonResourceKey.of(KryptonResourceKeys.DIMENSION, Key.key("the_nether"))
        @JvmField
        val END: ResourceKey<WorldDimension> = KryptonResourceKey.of(KryptonResourceKeys.DIMENSION, Key.key("the_end"))

        @JvmField
        val CODEC: Codec<WorldDimension> = RecordCodecBuilder.create { instance ->
            instance.group(
                KryptonDimensionType.CODEC.fieldOf("type").getting { it.type },
                // TODO: Add chunk generator codec when it exists
            ).apply(instance, RecordCodecBuilder.stable(Function { WorldDimension(it) }))
        }
    }
}

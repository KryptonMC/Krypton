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
package org.kryptonmc.krypton.util

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.effect.particle.BlockParticleType
import org.kryptonmc.api.effect.particle.BlockParticleData
import org.kryptonmc.api.effect.particle.DustParticleType
import org.kryptonmc.api.effect.particle.DustParticleData
import org.kryptonmc.api.effect.particle.DustTransitionParticleType
import org.kryptonmc.api.effect.particle.DustTransitionParticleData
import org.kryptonmc.api.effect.particle.ItemParticleType
import org.kryptonmc.api.effect.particle.ItemParticleData
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.VibrationParticleType
import org.kryptonmc.api.effect.particle.VibrationParticleData
import org.kryptonmc.krypton.registry.InternalRegistries

@Suppress("UNCHECKED_CAST")
fun ParticleEffect.write(buf: ByteBuf) {
    when (type) {
        is BlockParticleType -> buf.writeVarInt((data as BlockParticleData).block.stateId)
        is DustParticleType -> {
            val data = data as DustParticleData
            buf.writeFloat(if (data.color.red == 0.toShort()) Float.MIN_VALUE else data.color.red.toFloat() / 255F)
            buf.writeFloat(data.color.green.toFloat() / 255F)
            buf.writeFloat(data.color.blue.toFloat() / 255F)
            buf.writeFloat(data.scale)
        }
        is DustTransitionParticleType -> {
            val data = data as DustTransitionParticleData
            buf.writeFloat(if (data.from.red == 0.toShort()) Float.MIN_VALUE else data.from.red.toFloat() / 255F)
            buf.writeFloat(data.from.green.toFloat() / 255F)
            buf.writeFloat(data.from.blue.toFloat() / 255F)
            buf.writeFloat(data.scale)
            buf.writeFloat(if (data.to.red == 0.toShort()) Float.MIN_VALUE else data.to.red.toFloat() / 255F)
            buf.writeFloat(data.to.green.toFloat() / 255F)
            buf.writeFloat(data.to.blue.toFloat() / 255F)
        }
        is ItemParticleType -> buf.writeVarInt(InternalRegistries.ITEM.idOf((data as ItemParticleData).item))
        is VibrationParticleType -> {
            val data = data as VibrationParticleData
            buf.writeDouble(data.origin.x)
            buf.writeDouble(data.origin.y)
            buf.writeDouble(data.origin.z)
            buf.writeDouble(data.destination.x)
            buf.writeDouble(data.destination.y)
            buf.writeDouble(data.destination.z)
            buf.writeInt(data.ticks)
        }
        else -> Unit
    }
}

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
package org.kryptonmc.krypton.effect.particle

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.effect.particle.BlockParticleType
import org.kryptonmc.api.effect.particle.DustParticleType
import org.kryptonmc.api.effect.particle.DustTransitionParticleType
import org.kryptonmc.api.effect.particle.ItemParticleType
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.VibrationParticleType
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.krypton.effect.particle.data.KryptonBlockParticleData
import org.kryptonmc.krypton.effect.particle.data.KryptonDustParticleData
import org.kryptonmc.krypton.effect.particle.data.KryptonDustTransitionParticleData
import org.kryptonmc.krypton.effect.particle.data.KryptonItemParticleData
import org.kryptonmc.krypton.effect.particle.data.KryptonVibrationParticleData
import org.kryptonmc.krypton.util.readItem
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.world.block.KryptonBlock

fun ParticleType.createData(buf: ByteBuf): ParticleData? = when (this) {
    is BlockParticleType -> KryptonBlockParticleData(KryptonBlock.stateFromId(buf.readVarInt()))
    is DustParticleType -> KryptonDustParticleData(buf)
    is DustTransitionParticleType -> KryptonDustTransitionParticleData(buf)
    is ItemParticleType -> KryptonItemParticleData(buf.readItem())
    is VibrationParticleType -> KryptonVibrationParticleData(buf)
    else -> null
}

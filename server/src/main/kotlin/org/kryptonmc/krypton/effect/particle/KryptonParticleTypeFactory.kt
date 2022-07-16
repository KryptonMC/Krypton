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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.particle.BlockParticleType
import org.kryptonmc.api.effect.particle.ColorParticleType
import org.kryptonmc.api.effect.particle.DirectionalParticleType
import org.kryptonmc.api.effect.particle.DustParticleType
import org.kryptonmc.api.effect.particle.DustTransitionParticleType
import org.kryptonmc.api.effect.particle.ItemParticleType
import org.kryptonmc.api.effect.particle.NoteParticleType
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.SimpleParticleType
import org.kryptonmc.api.effect.particle.VibrationParticleType
import java.util.function.Function
import java.util.function.Supplier

object KryptonParticleTypeFactory : ParticleType.Factory {

    private val FACTORIES = mapOf<Class<out ParticleType>, Function<Key, out ParticleType>>(
        BlockParticleType::class.java to Function(::KryptonBlockParticleType),
        ColorParticleType::class.java to Function(::KryptonColorParticleType),
        DirectionalParticleType::class.java to Function(::KryptonDirectionalParticleType),
        DustParticleType::class.java to Function(::KryptonDustParticleType),
        DustTransitionParticleType::class.java to Function(::KryptonDustTransitionParticleType),
        ItemParticleType::class.java to Function(::KryptonItemParticleType),
        NoteParticleType::class.java to Function(::KryptonNoteParticleType),
        SimpleParticleType::class.java to Function(::KryptonSimpleParticleType),
        VibrationParticleType::class.java to Function(::KryptonVibrationParticleType)
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ParticleType> of(type: Class<T>, key: Key): T = requireNotNull(FACTORIES[type]) {
        "Could not find factory for type $type!"
    }.apply(key) as T
}

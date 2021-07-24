package org.kryptonmc.krypton.effect

import com.mojang.serialization.Codec
import org.kryptonmc.api.effect.particle.Particle
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.util.KEY_CODEC

val PARTICLE_CODEC: Codec<Particle> = KEY_CODEC.xmap({ Registries.PARTICLE_TYPE[it] }, { it.key })

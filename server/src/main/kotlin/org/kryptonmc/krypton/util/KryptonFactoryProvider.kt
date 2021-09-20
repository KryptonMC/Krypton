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

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.block.BlockHitResult
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.command.meta.CommandMeta
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.util.FactoryNotFoundException
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.krypton.auth.KryptonGameProfileFactory
import org.kryptonmc.krypton.command.meta.KryptonCommandMeta
import org.kryptonmc.krypton.effect.particle.KryptonParticleEffectFactory
import org.kryptonmc.krypton.effect.particle.data.KryptonParticleDataFactory
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import org.kryptonmc.krypton.item.KryptonItemStackFactory
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.krypton.world.block.KryptonBlockHitResultFactory
import org.kryptonmc.krypton.world.block.property.KryptonPropertyFactory

class KryptonFactoryProvider : FactoryProvider {

    private val factories = Object2ObjectOpenHashMap<Class<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(type: Class<T>): T = factories[type] as? T
        ?: throw FactoryNotFoundException("Type $type has no factory registered!")

    fun <T> register(clazz: Class<T>, factory: T) {
        require(clazz !in factories) { "Duplicate registration for type $clazz!" }
        factories[clazz] = factory
    }

    fun bootstrap() {
        register<ParticleData.Factory>(KryptonParticleDataFactory)
        register<ParticleEffect.Factory>(KryptonParticleEffectFactory)
        register<BlockHitResult.Factory>(KryptonBlockHitResultFactory)
        register<AttributeModifier.Factory>(KryptonAttributeModifier.Factory)
        register<Property.Factory>(KryptonPropertyFactory)
        register<RegistryManager>(KryptonRegistryManager)
        BlockLoader.init()
        register<ItemStack.Factory>(KryptonItemStackFactory)
        register<CommandMeta.Factory>(KryptonCommandMeta.Factory)
        register<GameProfile.Factory>(KryptonGameProfileFactory)
    }
}

inline fun <reified T> KryptonFactoryProvider.register(factory: T) = register(T::class.java, factory)

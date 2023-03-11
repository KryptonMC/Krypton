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
package org.kryptonmc.krypton.world.damage

import net.kyori.adventure.text.Component
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.world.damage.DamageSource
import org.kryptonmc.api.world.damage.type.DamageType
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer

open class KryptonDamageSource(override val type: DamageType) : DamageSource {

    constructor(type: RegistryReference<DamageType>) : this(type.get())

    fun damagesHelmet(): Boolean = type.damagesHelmet

    fun bypassesArmor(): Boolean = type.bypassesArmor

    fun bypassesInvulnerability(): Boolean = type.bypassesInvulnerability

    fun bypassesMagic(): Boolean = type.bypassesMagic

    fun exhaustion(): Float = type.exhaustion

    fun isFire(): Boolean = type.isFire

    fun isProjectile(): Boolean = type.isProjectile

    open fun scalesWithDifficulty(): Boolean = type.scalesWithDifficulty

    fun isMagic(): Boolean = type.isMagic

    fun isExplosion(): Boolean = type.isExplosion

    fun isFall(): Boolean = type.isFall

    fun isThorns(): Boolean = type.isThorns

    fun aggravatesTarget(): Boolean = type.aggravatesTarget

    fun isCreativePlayer(): Boolean {
        val entity = entity()
        return entity is KryptonPlayer && entity.abilities.canInstantlyBuild
    }

    open fun sourcePosition(): Position? = null

    open fun directEntity(): KryptonEntity? = entity()

    open fun entity(): KryptonEntity? = null

    open fun formatDeathMessage(target: KryptonLivingEntity): Component {
        val killer = target.killCredit()
        val baseMessage = "death.attack.${type.translationKey()}"
        if (killer != null) return Component.translatable("$baseMessage.player", target.displayName, killer.displayName)
        return Component.translatable(baseMessage, target.displayName)
    }
}

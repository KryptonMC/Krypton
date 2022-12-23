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
package org.kryptonmc.krypton.world.damage

import net.kyori.adventure.text.Component
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.api.util.Vec3d
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

    open fun sourcePosition(): Vec3d? = null

    open fun directEntity(): KryptonEntity? = entity()

    open fun entity(): KryptonEntity? = null

    open fun formatDeathMessage(target: KryptonLivingEntity): Component {
        val killer = target.killCredit()
        val baseMessage = "death.attack.${type.translationKey()}"
        if (killer != null) return Component.translatable("$baseMessage.player", target.displayName, killer.displayName)
        return Component.translatable(baseMessage, target.displayName)
    }
}

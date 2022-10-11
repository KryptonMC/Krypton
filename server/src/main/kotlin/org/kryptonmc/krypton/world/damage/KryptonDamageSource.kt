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
import org.kryptonmc.api.world.damage.DamageSource
import org.kryptonmc.api.world.damage.type.DamageType
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.spongepowered.math.vector.Vector3d

open class KryptonDamageSource(override val type: DamageType) : DamageSource {

    val damagesHelmet: Boolean
        get() = type.damagesHelmet
    val bypassesArmor: Boolean
        get() = type.bypassesArmor
    val bypassesInvulnerability: Boolean
        get() = type.bypassesInvulnerability
    val bypassesMagic: Boolean
        get() = type.bypassesMagic
    val exhaustion: Float
        get() = type.exhaustion
    val isFire: Boolean
        get() = type.isFire
    val isProjectile: Boolean
        get() = type.isProjectile
    open val scalesWithDifficulty: Boolean
        get() = type.scalesWithDifficulty
    val isMagic: Boolean
        get() = type.isMagic
    val isExplosion: Boolean
        get() = type.isExplosion
    val isFall: Boolean
        get() = type.isFall
    val isThorns: Boolean
        get() = type.isThorns
    val aggravatesTarget: Boolean
        get() = type.aggravatesTarget

    val isCreativePlayer: Boolean
        get() = entity() is KryptonPlayer && (entity() as KryptonPlayer).canInstantlyBuild
    open val sourcePosition: Vector3d?
        get() = null

    open fun directEntity(): KryptonEntity? = entity()

    open fun entity(): KryptonEntity? = null

    open fun formatDeathMessage(target: KryptonLivingEntity): Component {
        val killer = target.killCredit
        val baseMessage = "death.attack.${type.translationKey()}"
        if (killer != null) return Component.translatable("$baseMessage.player", target.displayName, killer.displayName)
        return Component.translatable(baseMessage, target.displayName)
    }
}

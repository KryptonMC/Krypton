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
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.world.damage.EntityDamageSource
import org.kryptonmc.api.world.damage.type.DamageType
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.spongepowered.math.vector.Vector3d

open class KryptonEntityDamageSource(
    type: DamageType,
    final override val entity: KryptonEntity
) : KryptonDamageSource(type), EntityDamageSource {

    override val isCreativePlayer: Boolean
        get() = entity is Player && entity.canInstantlyBuild
    override val sourcePosition: Vector3d?
        get() = entity.location
    override val scalesWithDifficulty: Boolean
        get() = entity is LivingEntity && entity !is Player

    override fun formatDeathMessage(target: KryptonLivingEntity): Component {
        val heldItem = target.heldItem(Hand.MAIN)
        val itemName = heldItem.meta.name
        if (!heldItem.isEmpty() && itemName != null) {
            return Component.translatable("${type.translationKey}.item", target.displayName, entity.displayName, itemName)
        }
        return Component.translatable(type.translationKey, target.displayName, entity.displayName)
    }
}

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
package org.kryptonmc.krypton.data.provider.entity

import org.kryptonmc.api.data.Keys
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.projectile.KryptonArrowLike

object ArrowLikeData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerFlag<KryptonArrowLike>(Keys.IS_CRITICAL, MetadataKeys.ARROW_LIKE.FLAGS, 0)
        registrar.registerFlag<KryptonArrowLike>(Keys.IGNORES_PHYSICS, MetadataKeys.ARROW_LIKE.FLAGS, 1)
        registrar.registerFlag<KryptonArrowLike>(Keys.WAS_SHOT_FROM_CROSSBOW, MetadataKeys.ARROW_LIKE.FLAGS, 2)
        registrar.registerMetadata<KryptonArrowLike, _, _>(Keys.PIERCING_LEVEL, MetadataKeys.ARROW_LIKE.PIERCING_LEVEL, Byte::toInt, Int::toByte)
        registrar.registerMutable(Keys.BASE_DAMAGE, KryptonArrowLike::baseDamage) { holder, value -> holder.baseDamage = value }
        registrar.registerMutable(Keys.STUCK_IN_BLOCK, KryptonArrowLike::stuckInBlock) { holder, value -> holder.stuckInBlock = value }
        registrar.registerMutable(Keys.IS_IN_GROUND, KryptonArrowLike::isInGround) { holder, value -> holder.isInGround = value }
        registrar.registerMutable(Keys.PICKUP_STATE, KryptonArrowLike::pickupState)
    }
}

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
package org.kryptonmc.krypton.world.damage

import net.kyori.adventure.text.Component
import org.kryptonmc.api.item.meta.MetaKeys
import org.kryptonmc.api.world.damage.IndirectEntityDamageSource
import org.kryptonmc.api.world.damage.type.DamageType
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.item.EmptyItemStack

class KryptonIndirectEntityDamageSource(
    type: DamageType,
    entity: KryptonEntity,
    override val indirectEntity: KryptonEntity
) : KryptonEntityDamageSource(type, entity), IndirectEntityDamageSource {

    override fun formatDeathMessage(target: KryptonLivingEntity): Component {
        val heldItem = EmptyItemStack // TODO: When living entities have hand items, use them here
        val itemName = heldItem.meta[MetaKeys.NAME]
        if (!heldItem.isEmpty() && itemName != null) {
            return Component.translatable("${type.translationKey}.item", target.displayName, indirectEntity.displayName, itemName)
        }
        return Component.translatable(type.translationKey, target.displayName, indirectEntity.displayName)
    }
}

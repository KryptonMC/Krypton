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
package org.kryptonmc.krypton.entity.animal

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.animal.Tamable
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.KryptonWorld

abstract class KryptonTamable(
    world: KryptonWorld,
    type: EntityType<out Tamable>,
    attributeSupplier: AttributeSupplier
) : KryptonAnimal(world, type, attributeSupplier), Tamable {

    final override var isOrderedToSit: Boolean = false
    final override var isSitting: Boolean
        get() = getFlag(MetadataKeys.Tamable.FLAGS, FLAG_SITTING)
        set(value) = setFlag(MetadataKeys.Tamable.FLAGS, FLAG_SITTING, value)
    override var isTamed: Boolean
        get() = getFlag(MetadataKeys.Tamable.FLAGS, FLAG_TAMED)
        set(value) = setFlag(MetadataKeys.Tamable.FLAGS, FLAG_TAMED, value)
    final override val owner: KryptonPlayer?
        get() {
            val uuid = data.get(MetadataKeys.Tamable.OWNER) ?: return null
            return world.entityManager[uuid] as? KryptonPlayer
        }
    final override val team: Team?
        get() {
            if (isTamed) return owner?.team
            return super.team
        }

    init {
        data.add(MetadataKeys.Tamable.FLAGS, 0)
        data.add(MetadataKeys.Tamable.OWNER, null)
    }

    final override fun tame(tamer: Player) {
        isTamed = true
        data.set(MetadataKeys.Tamable.OWNER, tamer.uuid)
        // TODO: Trigger tame animal advancement criteria
    }

    companion object {

        private const val FLAG_SITTING = 0
        private const val FLAG_TAMED = 2
    }
}

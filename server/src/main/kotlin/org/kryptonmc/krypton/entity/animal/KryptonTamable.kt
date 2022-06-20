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
import java.util.UUID

abstract class KryptonTamable(
    world: KryptonWorld,
    type: EntityType<out Tamable>,
    attributeSupplier: AttributeSupplier
) : KryptonAnimal(world, type, attributeSupplier), Tamable {

    final override var isOrderedToSit: Boolean = false
    override var isTame: Boolean
        get() = getFlag(2)
        set(value) = setFlag(2, value)
    final override var isSitting: Boolean
        get() = getFlag(0)
        set(value) = setFlag(0, value)
    private var ownerUUID: UUID?
        get() = data[MetadataKeys.TAMABLE.OWNER]
        set(value) = data.set(MetadataKeys.TAMABLE.OWNER, value)
    final override val owner: KryptonPlayer?
        get() {
            val uuid = ownerUUID ?: return null
            return world.players.firstOrNull { it.uuid == uuid }
        }
    final override val team: Team?
        get() {
            if (isTame) return owner?.team
            return super.team
        }

    init {
        data.add(MetadataKeys.TAMABLE.FLAGS)
        data.add(MetadataKeys.TAMABLE.OWNER)
    }

    final override fun tame(tamer: Player) {
        isTame = true
        ownerUUID = tamer.uuid
        // TODO: Trigger tame animal advancement criteria
    }

    private fun getFlag(flag: Int): Boolean = getFlag(MetadataKeys.TAMABLE.FLAGS, flag)

    private fun setFlag(flag: Int, state: Boolean) {
        setFlag(MetadataKeys.TAMABLE.FLAGS, flag, state)
    }
}

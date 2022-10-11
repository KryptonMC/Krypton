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

import org.kryptonmc.api.entity.animal.Tamable
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.TamableSerializer
import org.kryptonmc.krypton.world.KryptonWorld

abstract class KryptonTamable(world: KryptonWorld) : KryptonAnimal(world), Tamable {

    override val serializer: EntitySerializer<out KryptonTamable>
        get() = TamableSerializer

    final override var isOrderedToSit: Boolean = false
    final override var isSitting: Boolean
        get() = data.getFlag(MetadataKeys.Tamable.FLAGS, FLAG_SITTING)
        set(value) = data.setFlag(MetadataKeys.Tamable.FLAGS, FLAG_SITTING, value)
    override var isTamed: Boolean
        get() = data.getFlag(MetadataKeys.Tamable.FLAGS, FLAG_TAMED)
        set(value) = data.setFlag(MetadataKeys.Tamable.FLAGS, FLAG_TAMED, value)
    final override val owner: KryptonPlayer?
        get() = data.get(MetadataKeys.Tamable.OWNER)?.let(world.entityManager::get) as? KryptonPlayer
    final override val team: Team?
        get() = if (isTamed) owner?.team else super.team

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Tamable.FLAGS, 0)
        data.define(MetadataKeys.Tamable.OWNER, null)
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

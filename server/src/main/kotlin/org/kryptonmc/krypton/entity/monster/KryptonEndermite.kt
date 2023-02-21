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
package org.kryptonmc.krypton.entity.monster

import org.kryptonmc.api.entity.monster.Endermite
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.monster.EndermiteSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonEndermite(world: KryptonWorld) : KryptonMonster(world), Endermite {

    override val type: KryptonEntityType<KryptonEndermite>
        get() = KryptonEntityTypes.ENDERMITE
    override val serializer: EntitySerializer<KryptonEndermite>
        get() = EndermiteSerializer

    override var life: Int = 2400
    override var remainingLife: Int = life

    override fun tick() {
        super.tick()
        tickLife()
    }

    private fun tickLife() {
        if (!isPersistent) remainingLife--
        if (remainingLife <= 0) remove()
    }

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMonster.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, 8.0)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, 0.25)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, 2.0)
    }
}

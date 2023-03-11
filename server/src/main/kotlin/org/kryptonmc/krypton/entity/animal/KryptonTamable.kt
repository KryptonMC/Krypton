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
        get() = data.get(MetadataKeys.Tamable.OWNER)?.let(world.entityManager::getByUUID) as? KryptonPlayer
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

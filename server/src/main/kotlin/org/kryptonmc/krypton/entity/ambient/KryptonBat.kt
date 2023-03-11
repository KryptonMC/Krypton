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
package org.kryptonmc.krypton.entity.ambient

import org.kryptonmc.api.entity.ambient.Bat
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.ambient.BatSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonBat(world: KryptonWorld) : KryptonAmbientCreature(world), Bat {

    override val type: KryptonEntityType<KryptonBat>
        get() = KryptonEntityTypes.BAT
    override val serializer: EntitySerializer<KryptonBat>
        get() = BatSerializer

    override var isResting: Boolean
        get() = data.getFlag(MetadataKeys.Bat.FLAGS, FLAG_RESTING)
        set(value) = data.setFlag(MetadataKeys.Bat.FLAGS, FLAG_RESTING, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Bat.FLAGS, 0)
    }

    companion object {

        private const val FLAG_RESTING = 0
        private const val DEFAULT_MAX_HEALTH = 6.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes().add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
    }
}

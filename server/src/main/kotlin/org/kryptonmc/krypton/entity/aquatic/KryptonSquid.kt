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
package org.kryptonmc.krypton.entity.aquatic

import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.world.KryptonWorld

open class KryptonSquid(world: KryptonWorld) : KryptonAquaticAnimal(world) {

    override val type: KryptonEntityType<KryptonSquid>
        get() = KryptonEntityTypes.SQUID

    override fun soundVolume(): Float = SOUND_VOLUME

    companion object {

        private const val SOUND_VOLUME = 0.4F
        private const val DEFAULT_MAX_HEALTH = 10.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes().add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
    }
}

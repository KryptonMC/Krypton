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
package org.kryptonmc.krypton.entity.projectile

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.projectile.FireworkRocket
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.OptionalInt

class KryptonFireworkRocket(world: KryptonWorld) : KryptonProjectile(world), FireworkRocket {

    override val type: KryptonEntityType<KryptonFireworkRocket>
        get() = KryptonEntityTypes.FIREWORK_ROCKET

    override var attachedEntity: Entity? = null
    override var life: Int = 0
    override var lifetime: Int = 0

    override var wasShotAtAngle: Boolean
        get() = data.get(MetadataKeys.FireworkRocket.SHOT_AT_ANGLE)
        set(value) = data.set(MetadataKeys.FireworkRocket.SHOT_AT_ANGLE, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.FireworkRocket.ITEM, KryptonItemStack.EMPTY)
        data.define(MetadataKeys.FireworkRocket.ATTACHED, OptionalInt.empty())
        data.define(MetadataKeys.FireworkRocket.SHOT_AT_ANGLE, false)
    }

    override fun asItem(): ItemStack {
        val item = data.get(MetadataKeys.FireworkRocket.ITEM)
        if (item.isEmpty()) return DEFAULT_ITEM
        return item
    }

    companion object {

        private val DEFAULT_ITEM = KryptonItemStack(ItemTypes.FIREWORK_ROCKET.get())
    }
}

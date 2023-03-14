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

import org.kryptonmc.api.entity.projectile.Arrow
import org.kryptonmc.api.util.Color
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.projectile.ArrowSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonArrow(world: KryptonWorld) : KryptonArrowLike(world), Arrow {

    override val type: KryptonEntityType<KryptonArrow>
        get() = KryptonEntityTypes.ARROW
    override val serializer: EntitySerializer<KryptonArrow>
        get() = ArrowSerializer

    override var color: Color
        get() = Color(data.get(MetadataKeys.Arrow.COLOR))
        set(value) = data.set(MetadataKeys.Arrow.COLOR, value.encode())

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Arrow.COLOR, -1)
    }
}

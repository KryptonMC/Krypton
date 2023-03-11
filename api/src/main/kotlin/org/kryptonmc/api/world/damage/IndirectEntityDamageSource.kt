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
package org.kryptonmc.api.world.damage

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.world.damage.type.DamageType
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A damage source that indirectly affects an entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface IndirectEntityDamageSource : EntityDamageSource {

    /**
     * The entity that indirectly caused the damage.
     */
    @get:JvmName("indirectEntity")
    public val indirectEntity: Entity?

    public companion object {

        /**
         * Creates a new damage source that causes the given [type] of damage,
         * and that is caused directly by the given [entity], and indirectly by
         * the given [indirectEntity].
         *
         * @param type the type of damage the source will inflict
         * @param entity the entity that directly caused the damage
         * @param indirectEntity the entity that indirectly caused the damage
         * @return a new indirect entity damage source
         */
        @JvmStatic
        @Contract("_, _, _ -> new", pure = true)
        public fun of(type: DamageType, entity: Entity, indirectEntity: Entity): IndirectEntityDamageSource =
            Krypton.factory<DamageSource.Factory>().indirectEntity(type, entity, indirectEntity)
    }
}

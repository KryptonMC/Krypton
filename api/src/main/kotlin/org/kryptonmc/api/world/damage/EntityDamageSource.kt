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
 * A damage source that affects an entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface EntityDamageSource : DamageSource {

    /**
     * The entity that caused the damage.
     */
    @get:JvmName("entity")
    public val entity: Entity

    public companion object {

        /**
         * Creates a new damage source with the given [type], where the damage
         * originated from the given [entity].
         *
         * @param type the type of damage the source will cause
         * @param entity the source of the damage
         * @return a new entity damage source
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(type: DamageType, entity: Entity): EntityDamageSource = Krypton.factory<DamageSource.Factory>().entity(type, entity)
    }
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.damage

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.world.damage.type.DamageType

/**
 * A damage source that affects an entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
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

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
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.world.damage.type.DamageType

/**
 * A damage source that indirectly affects an entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface IndirectEntityDamageSource : EntityDamageSource {

    /**
     * The entity that indirectly caused the damage.
     */
    @get:JvmName("indirectEntity")
    public val indirectEntity: Entity

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
        @Contract("_ -> new", pure = true)
        public fun of(type: DamageType, entity: Entity, indirectEntity: Entity): IndirectEntityDamageSource =
            DamageSource.FACTORY.indirectEntity(type, entity, indirectEntity)
    }
}

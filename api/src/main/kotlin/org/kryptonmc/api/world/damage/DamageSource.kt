/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.damage

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.util.provide
import org.kryptonmc.api.world.damage.type.DamageType

/**
 * A source of damage to something.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DamageSource {

    /**
     * The type of damage that has been caused by this source.
     */
    @get:JvmName("type")
    public val type: DamageType

    @ApiStatus.Internal
    public interface Factory {

        public fun of(type: DamageType): DamageSource

        public fun entity(type: DamageType, entity: Entity): EntityDamageSource

        public fun indirectEntity(type: DamageType, entity: Entity, indirectEntity: Entity?): IndirectEntityDamageSource
    }

    public companion object {

        @JvmSynthetic
        internal val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new damage source with the given [type].
         *
         * @param type the type
         * @return a new damage source
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(type: DamageType): DamageSource = FACTORY.of(type)
    }
}

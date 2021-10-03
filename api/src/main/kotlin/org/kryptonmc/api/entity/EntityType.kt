/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.TranslatableComponent
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.TranslationHolder
import org.kryptonmc.api.util.provide

/**
 * A type of entity.
 *
 * @param T the type of entity
 */
@CataloguedBy(EntityTypes::class)
public interface EntityType<T : Entity> : TranslationHolder, Keyed {

    /**
     * If this entity type can be summoned, with the /summon command, or by
     * spawning the entity through [org.kryptonmc.api.world.World.spawnEntity].
     */
    public val isSummonable: Boolean

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun <T : Entity> of(key: Key, summonable: Boolean, translation: TranslatableComponent): EntityType<T>

        public fun <T : Entity> of(key: Key, summonable: Boolean): EntityType<T>
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new entity type with the given values.
         *
         * @param key the key
         * @param summonable if entities of the type can be summoned
         * @param translation the translation
         * @return a new entity type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun <T : Entity> of(
            key: Key,
            summonable: Boolean,
            translation: TranslatableComponent
        ): EntityType<T> = FACTORY.of(key, summonable, translation)

        /**
         * Creates a new entity type with the given values.
         *
         * @param key the key
         * @param summonable if entities of the type can be summoned
         * @return a new entity type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun <T : Entity> of(
            key: Key,
            summonable: Boolean,
        ): EntityType<T> = FACTORY.of(key, summonable)
    }
}

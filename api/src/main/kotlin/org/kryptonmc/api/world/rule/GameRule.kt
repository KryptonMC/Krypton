/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.rule

import net.kyori.adventure.text.TranslatableComponent
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.TranslationHolder
import org.kryptonmc.api.util.provide

/**
 * A rule dictating how a specific aspect of the game functions.
 *
 * @param V the type of the value
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(GameRules::class)
public interface GameRule<V : Any> : TranslationHolder {

    /**
     * The name of this rule.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The default value of this rule.
     */
    @get:JvmName("defaultValue")
    public val default: V

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun <V : Any> of(name: String, default: V, translation: TranslatableComponent): GameRule<V>
    }

    public companion object {

        private val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates a new game rule with the given values.
         *
         * @param name the name
         * @param default the default value
         * @param translation the client-side translation
         * @return a new game rule
         */
        @JvmStatic
        public fun <V : Any> of(
            name: String,
            default: V,
            translation: TranslatableComponent
        ): GameRule<V> = FACTORY.of(name, default, translation)
    }
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.TranslationHolder
import org.kryptonmc.api.util.provide

/**
 * A mode of the game.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(GameModes::class)
public interface GameMode : TranslationHolder {

    /**
     * The full name of this game mode. For example, 'survival'.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The abbreviation of this game mode. For example, 's' for survival.
     */
    @get:JvmName("abbreviation")
    public val abbreviation: String

    /**
     * If this game mode is permitted to build.
     */
    @get:JvmName("canBuild")
    public val canBuild: Boolean

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(name: String, abbreviation: String, canBuild: Boolean, translation: TranslatableComponent): GameMode
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new game mode with the given values.
         *
         * @param name the name
         * @param abbreviation the abbreviation
         * @param canBuild if the mode permits users using it to build
         * @param translation the translation
         * @return a new game mode
         */
        @JvmStatic
        @JvmOverloads
        @Contract("_ -> new", pure = true)
        public fun of(
            name: String,
            abbreviation: String,
            canBuild: Boolean,
            translation: TranslatableComponent = Component.translatable("gameMode.${name.lowercase()}")
        ): GameMode = FACTORY.of(name, abbreviation, canBuild, translation)
    }
}

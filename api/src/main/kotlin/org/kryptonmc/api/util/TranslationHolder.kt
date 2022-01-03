/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.translation.Translatable
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.Contract

/**
 * An object that holds a translation. The translation is usually the client-side translatable
 * component for the name of the object.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface TranslationHolder : Translatable, ComponentLike {

    /**
     * The translatable component that this translates to.
     */
    @get:JvmName("translation")
    public val translation: TranslatableComponent

    override fun translationKey(): String = translation.key()

    override fun asComponent(): Component = translation

    /**
     * A base builder for building translation holders.
     */
    public interface Builder<B : Builder<B, T>, T : TranslationHolder> : Buildable.Builder<T> {

        /**
         * Sets the translation of the translation holder to the given
         * [translation].
         *
         * @param translation the translation
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun translation(translation: TranslatableComponent): B
    }
}

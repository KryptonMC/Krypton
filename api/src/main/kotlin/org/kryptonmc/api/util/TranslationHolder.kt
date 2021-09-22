/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.TranslatableComponent

/**
 * An object that holds a translation. The translation is usually the client-side translatable
 * component for the name of the object.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface TranslationHolder : ComponentLike {

    /**
     * The translatable component that this translates to.
     */
    @get:JvmName("translation")
    public val translation: TranslatableComponent

    override fun asComponent(): Component = translation
}

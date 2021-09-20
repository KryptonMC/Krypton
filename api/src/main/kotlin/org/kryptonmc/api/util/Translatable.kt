package org.kryptonmc.api.util

import net.kyori.adventure.text.TranslatableComponent

/**
 * An object that can be translated, usually one where the translation is the name.
 */
public interface Translatable {

    /**
     * The translatable component that this translates to.
     */
    public val translation: TranslatableComponent
}

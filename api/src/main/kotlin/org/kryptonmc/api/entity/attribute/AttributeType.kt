/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.TranslatableComponent
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.TranslationHolder
import org.kryptonmc.api.util.provide

/**
 * The type of an attribute.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(AttributeTypes::class)
public interface AttributeType : TranslationHolder, Keyed {

    /**
     * The default base value for attributes of this type.
     */
    @get:JvmName("defaultBase")
    public val defaultBase: Double

    /**
     * The minimum base value for attributes of this type.
     */
    @get:JvmName("minimum")
    public val minimum: Double

    /**
     * The maximum base value for attributes of this type.
     */
    @get:JvmName("maximum")
    public val maximum: Double

    /**
     * If attributes of this type should be sent to clients.
     */
    public val sendToClient: Boolean

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(
            key: Key,
            defaultBase: Double,
            minimum: Double,
            maximum: Double,
            sendToClient: Boolean,
            translation: TranslatableComponent
        ): AttributeType
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new type of attribute with the given values.
         *
         * @param defaultBase the default base value
         * @param minimum the minimum base
         * @param maximum the maximum base
         * @param sendToClient if the type should be sent to clients
         * @return a new type of attribute
         */
        @JvmStatic
        public fun of(
            key: Key,
            defaultBase: Double,
            minimum: Double,
            maximum: Double,
            sendToClient: Boolean
        ): AttributeType = of(key, defaultBase, minimum, maximum, sendToClient, translatable("attribute.name.${key.value()}"))

        /**
         * Creates a new type of attribute with the given values.
         *
         * @param defaultBase the default base value
         * @param minimum the minimum base
         * @param maximum the maximum base
         * @param sendToClient if the type should be sent to clients
         * @param translation the translation
         * @return a new type of attribute
         */
        @JvmStatic
        public fun of(
            key: Key,
            defaultBase: Double,
            minimum: Double,
            maximum: Double,
            sendToClient: Boolean,
            translation: TranslatableComponent
        ): AttributeType = FACTORY.of(key, defaultBase, minimum, maximum, sendToClient, translation)
    }
}

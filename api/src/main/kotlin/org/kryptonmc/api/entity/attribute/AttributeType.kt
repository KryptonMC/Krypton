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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent

/**
 * Represents a type of an attribute.
 *
 * @param key the key
 * @param sendToClient if attributes of this type should be sent to clients
 * @param defaultBase the default base value
 * @param minimum the minimum base value
 * @param maximum the maximum base value
 * @param translation the translation
 */
@JvmRecord
public data class AttributeType(
    @get:JvmName("_get-key") @JvmSynthetic public val key: Key,
    @get:JvmName("sendToClient") public val sendToClient: Boolean,
    public val defaultBase: Double,
    public val minimum: Double,
    public val maximum: Double,
    public val translation: TranslatableComponent = Component.translatable("attribute.name.${key.value()}")
) : Keyed {

    override fun key(): Key = key
}

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
data class AttributeType(
    val key: Key,
    val sendToClient: Boolean,
    val defaultBase: Double,
    val minimum: Double,
    val maximum: Double,
    val translation: TranslatableComponent = Component.translatable("attribute.name.${key.value()}")
) : Keyed {

    override fun key() = key
}

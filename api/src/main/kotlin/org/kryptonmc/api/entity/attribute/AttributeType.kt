/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

import net.kyori.adventure.key.Keyed
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.api.util.CataloguedBy
import javax.annotation.concurrent.Immutable

/**
 * The type of an attribute.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(AttributeTypes::class)
@Immutable
public interface AttributeType : Translatable, Keyed {

    /**
     * The default value for attributes of this type.
     */
    @get:JvmName("defaultValue")
    public val defaultValue: Double

    /**
     * Ensures that the given [value] satisfies the constraints of this
     * attribute type.
     *
     * For example, with ranged attribute types, this will ensure that the
     * value is between the minimum and maximum value.
     *
     * @param value the value to sanitize
     * @return the sanitized result
     */
    public fun sanitizeValue(value: Double): Double
}

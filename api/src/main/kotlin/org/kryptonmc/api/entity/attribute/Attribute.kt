/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

import java.util.UUID

/**
 * Represents an attribute that can be applied to a living entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Attribute {

    /**
     * The type of this attribute.
     */
    @get:JvmName("type")
    public val type: AttributeType

    /**
     * The base value of this attribute.
     */
    @get:JvmName("baseValue")
    public var baseValue: Double

    /**
     * The cached calculated value of this attribute.
     */
    @get:JvmName("value")
    public val value: Double

    /**
     * The modifiers used to modify the [baseValue] in calculation to get the
     * [value].
     */
    @get:JvmName("modifiers")
    public val modifiers: Set<AttributeModifier>

    /**
     * Gets the modifier with the given [uuid], or returns null if there is no
     * modifier with the given [uuid].
     *
     * @param uuid the UUID
     * @return the modifier, or null if not present
     */
    public fun modifier(uuid: UUID): AttributeModifier?

    /**
     * Gets all modifiers stored under the given [operation].
     *
     * @param operation the operation
     * @return all modifiers for the given operation
     */
    public fun modifiers(operation: ModifierOperation): Set<AttributeModifier>

    /**
     * Adds the given [modifier] to the list of modifiers under the given
     * [operation].
     *
     * @param operation the operation
     * @param modifier the modifier to add
     */
    public fun addModifier(operation: ModifierOperation, modifier: AttributeModifier)

    /**
     * Removes the given [modifier] from the list of modifiers under the given
     * [operation].
     *
     * @param operation the operation
     * @param modifier the modifier to remove
     */
    public fun removeModifier(operation: ModifierOperation, modifier: AttributeModifier)

    /**
     * Clears all modifiers for this attribute.
     */
    public fun clearModifiers()
}

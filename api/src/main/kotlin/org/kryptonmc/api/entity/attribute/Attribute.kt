/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

import java.util.UUID

/**
 * Represents an attribute that can be applied to a living entity.
 */
public interface Attribute {

    /**
     * The type of this attribute.
     */
    public val type: AttributeType

    /**
     * The base value of this attribute.
     */
    public var baseValue: Double

    /**
     * The modifiers used to modify the [baseValue] in calculation to get the
     * value.
     */
    public val modifiers: Collection<AttributeModifier>

    /**
     * Calculates the final value of this attribute by applying the modifiers
     * to the base value.
     *
     * @return the final value of this attribute
     */
    public fun calculateValue(): Double

    /**
     * Gets the modifier with the given [uuid], or returns null if there is no
     * modifier with the given [uuid].
     *
     * @param uuid the UUID
     * @return the modifier, or null if not present
     */
    public fun getModifier(uuid: UUID): AttributeModifier?

    /**
     * Gets all modifiers stored under the given [operation].
     *
     * @param operation the operation
     * @return all modifiers for the given operation
     */
    public fun getModifiers(operation: ModifierOperation): Set<AttributeModifier>

    /**
     * Adds the given [modifier] to the list of modifiers.
     *
     * @param modifier the modifier to add
     */
    public fun addModifier(modifier: AttributeModifier)

    /**
     * Removes the given [modifier] from the list of modifiers.
     *
     * @param modifier the modifier to remove
     */
    public fun removeModifier(modifier: AttributeModifier)

    /**
     * Clears all modifiers for this attribute.
     */
    public fun clearModifiers()
}

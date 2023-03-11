/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

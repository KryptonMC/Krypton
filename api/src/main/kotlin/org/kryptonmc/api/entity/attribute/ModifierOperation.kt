/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

/**
 * An operation that can be applied to attribute modifiers.
 *
 * See [BasicModifierOperation] for the basic built-in operations.
 */
public fun interface ModifierOperation {

    /**
     * Applies this operation to the given [modifiers] and returns the result.
     *
     * @param modifiers the modifiers to apply
     * @return the result of applying this operation to the given modifiers
     */
    public fun apply(base: Double, modifiers: Collection<AttributeModifier>): Double
}

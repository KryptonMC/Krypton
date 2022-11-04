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
 */
public enum class BasicModifierOperation : ModifierOperation {

    /**
     * Adds each modifier's amount to the base value.
     *
     * For example, with a base of 3, and 2 modifiers, with values 2 and 4
     * respectively, the total would be 9, because 3 + 2 + 4 = 9.
     */
    ADDITION {

        override fun apply(base: Double, modifiers: Collection<AttributeModifier>): Double =
            modifiers.fold(base) { acc, modifier -> acc + modifier.amount }
    },

    /**
     * Multiplies the base value by 1 + the sum of all the modifier amounts.
     *
     * For example, with a base of 3 and 2 modifiers with values 2 and 4
     * respectively, the total would be 21, because 3 * (1 + 2 + 4) = 21.
     */
    MULTIPLY_BASE {

        override fun apply(base: Double, modifiers: Collection<AttributeModifier>): Double =
            base * modifiers.fold(1.0) { acc, modifier -> acc + modifier.amount }
    },

    /**
     * Multiplies the base by each value + 1.
     *
     * For example, with a base of 3 and 2 modifiers with values 2 and 4
     * respectively, the total would be 45, as 3 * (1 + 2) * (1 + 4) = 45.
     */
    MULTIPLY_TOTAL {

        override fun apply(base: Double, modifiers: Collection<AttributeModifier>): Double =
            modifiers.fold(base) { acc, modifier -> acc * (modifier.amount + 1) }
    };
}

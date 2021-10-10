/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

/**
 * Represents an operation that can be [applied][apply] to
 * [AttributeModifier]s.
 */
public fun interface ModifierOperation {

    /**
     * Applies this operation to the specified [values] and returns the result.
     *
     * @param values the values to apply
     * @return the result of applying this operation to the specified values
     */
    public fun apply(base: Double, values: Set<AttributeModifier>): Double

    public companion object {

        /**
         * Adds each modifier's amount to the base value.
         *
         * For example, with a base of 3, and 2 modifiers, with values 2 and 4
         * respectively, the total would be 9, because 3 + 2 + 4 = 9.
         */
        @JvmField
        public val ADD: ModifierOperation = ModifierOperation { base, values ->
            values.fold(base) { acc, value -> acc + value.amount }
        }

        /**
         * Multiplies the base value by 1 + the sum of all the modifier amounts.
         *
         * For example, with a base of 3 and 2 modifiers with values 2 and 4
         * respectively, the total would be 21, because 3 * (1 + 2 + 4) = 21.
         */
        @JvmField
        public val MULTIPLY_BASE: ModifierOperation = ModifierOperation { base, values ->
            base * values.fold(1.0) { acc, value -> acc + value.amount }
        }

        /**
         * Multiplies the base by each value + 1.
         *
         * For example, with a base of 3 and 2 modifiers with values 2 and 4
         * respectively, the total would be 45, as 3 * (1 + 2) * (1 + 4) = 45.
         */
        @JvmField
        public val MULTIPLY_TOTAL: ModifierOperation = ModifierOperation { base, values ->
            values.fold(base) { acc, value -> acc * (1 + value.amount) }
        }
    }
}

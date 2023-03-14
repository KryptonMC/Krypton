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

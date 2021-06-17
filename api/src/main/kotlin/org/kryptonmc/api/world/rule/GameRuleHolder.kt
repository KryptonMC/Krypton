/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.rule

/**
 * A holder of game rules. Used to get and set game rule values.
 */
interface GameRuleHolder {

    /**
     * All the rules this holder is holding
     */
    val rules: Map<GameRule<out Any>, Any>

    /**
     * Gets the value of the provided [rule].
     *
     * @param rule the rule
     * @param V the value type
     * @return the value
     */
    operator fun <V : Any> get(rule: GameRule<V>): V

    /**
     * Sets the value of the given [rule] to the given [value].
     *
     * @param rule the rule
     * @param value the new value
     */
    operator fun <V : Any> set(rule: GameRule<V>, value: V)
}

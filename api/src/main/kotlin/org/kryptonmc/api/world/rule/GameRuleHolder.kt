/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.rule

import org.kryptonmc.api.registry.RegistryReference

/**
 * A holder of game rules. Used to get and set game rule values.
 */
public interface GameRuleHolder {

    /**
     * All the rules this holder is holding.
     */
    public val rules: Map<GameRule<*>, *>

    /**
     * Gets the value of the provided [rule].
     *
     * @param V the value type
     * @param rule the rule
     * @return the value
     */
    public fun <V> get(rule: GameRule<V>): V

    /**
     * Gets the value of the provided [rule].
     *
     * @param V the value type
     * @param rule the rule
     * @return the value
     */
    public fun <V> get(rule: RegistryReference<GameRule<V>>): V = get(rule.get())

    /**
     * Sets the value of the given [rule] to the given [value].
     *
     * @param V the value type
     * @param rule the rule
     * @param value the new value
     */
    public fun <V> set(rule: GameRule<V>, value: V & Any)

    /**
     * Sets the value of the given [rule] to the given [value].
     *
     * @param V the value type
     * @param rule the rule
     * @param value the new value
     */
    public fun <V> set(rule: RegistryReference<GameRule<V>>, value: V & Any) {
        set(rule.get(), value)
    }
}

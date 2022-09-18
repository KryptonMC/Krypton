/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.rule

/**
 * A holder of game rules. Used to get and set game rule values.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface GameRuleHolder {

    /**
     * All the rules this holder is holding
     */
    @get:JvmName("rules")
    public val rules: Map<GameRule<*>, *>

    /**
     * Gets the value of the provided [rule].
     *
     * @param rule the rule
     * @param V the value type
     * @return the value
     */
    public fun <V : Any> get(rule: GameRule<V>): V

    /**
     * Sets the value of the given [rule] to the given [value].
     *
     * @param rule the rule
     * @param value the new value
     */
    public fun <V : Any> set(rule: GameRule<V>, value: V)
}

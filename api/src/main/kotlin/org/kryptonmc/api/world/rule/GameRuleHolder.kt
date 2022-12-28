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
public interface GameRuleHolder {

    /**
     * Gets the value of the given [rule] in this holder.
     *
     * @param V the value type
     * @param rule the rule
     * @return the value
     */
    public fun <V> getGameRule(rule: GameRule<V>): V

    /**
     * Sets the value of the given [rule] to the given [value] in this holder.
     *
     * @param V the value type
     * @param rule the rule
     * @param value the new value
     */
    public fun <V> setGameRule(rule: GameRule<V>, value: V & Any)
}

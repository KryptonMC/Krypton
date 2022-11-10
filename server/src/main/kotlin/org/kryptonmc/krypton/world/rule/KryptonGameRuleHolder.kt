/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.rule

import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.api.world.rule.GameRuleHolder
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound

// TODO: The way this works is a bit messy and I want to rewrite it
class KryptonGameRuleHolder private constructor(override val rules: MutableMap<GameRule<*>, Any>) : GameRuleHolder {

    constructor() : this(KryptonRegistries.GAME_RULES.associateWithTo(HashMap()) { it.defaultValue!! })

    fun save(): CompoundTag = compound {
        rules.forEach { (rule, value) -> putString(rule.name, value.toString()) }
    }

    @Suppress("UNCHECKED_CAST") // This should be fine
    override fun <V> get(rule: GameRule<V>): V = rules.getOrDefault(rule, rule.defaultValue) as V

    override fun <V> set(rule: GameRule<V>, value: V & Any) {
        rules.put(rule, value)
    }

    companion object {

        @JvmStatic
        fun from(data: CompoundTag): KryptonGameRuleHolder {
            val rules = HashMap<GameRule<*>, Any>()
            KryptonRegistries.GAME_RULES.forEach {
                if (!data.contains(it.name, StringTag.ID)) return@forEach
                rules.put(it, deserialize(data.getString(it.name)))
            }
            return KryptonGameRuleHolder(rules)
        }

        @JvmStatic
        private fun deserialize(input: String): Any {
            if (input.toBooleanStrictOrNull() != null) return input.toBooleanStrict()
            if (input.toIntOrNull() != null) return input.toInt()
            error("Game rules must be either booleans or integers, $input couldn't be parsed to either!")
        }
    }
}

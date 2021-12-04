/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import ca.spottedleaf.dataconverter.types.MapType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.api.world.rule.GameRuleHolder
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

class KryptonGameRuleHolder : GameRuleHolder {

    override val rules = mutableMapOf<GameRule<out Any>, Any>()

    constructor() {
        Registries.GAMERULES.values.forEach { rules[it] = it.default }
    }

    constructor(tag: MapType<String>) : this() {
        rules.forEach { (key, _) -> tag.getString(key.name)?.let { rules[key] = deserialize(it) } }
    }

    fun save(): CompoundTag = compound {
        rules.forEach { (rule, value) -> string(rule.name, value.toString()) }
    }

    @Suppress("UNCHECKED_CAST") // This should be fine
    override fun <V : Any> get(rule: GameRule<V>): V = rules.getOrDefault(rule, rule.default) as V

    override fun <V : Any> set(rule: GameRule<V>, value: V) {
        rules[rule] = value
    }

    companion object {

        @JvmStatic
        private fun deserialize(input: String): Any {
            if (input.toBooleanStrictOrNull() != null) return input.toBooleanStrict()
            if (input.toIntOrNull() != null) return input.toInt()
            error("Game rules must be either booleans or integers, $input couldn't be parsed to either!")
        }
    }
}

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
package org.kryptonmc.krypton.world

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.api.world.rule.GameRuleHolder

class KryptonGameRuleHolder : GameRuleHolder {

    override val rules = mutableMapOf<GameRule<out Any>, Any>()

    constructor() {
        Registries.GAMERULES.values.forEach { rules[it] = it.default }
    }

    constructor(tag: CompoundBinaryTag) {
        tag.forEach {
            val rule = GAME_RULES[it.key] ?: return@forEach
            val value = it.value as? StringBinaryTag ?: return@forEach
            if (value.value().toBooleanStrictOrNull() != null) return@forEach set(rule, value.value().toBooleanStrict())
            if (value.value().toIntOrNull() != null) return@forEach set(rule, value.value().toInt())
        }
    }

    override fun <V : Any> get(rule: GameRule<V>) = rules.getOrDefault(rule, rule.default) as V

    override fun <V : Any> set(rule: GameRule<V>, value: V) = rules.set(rule, value)
}

private val GAME_RULES = Registries.GAMERULES.values.associateBy { it.name }

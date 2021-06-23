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
package org.kryptonmc.krypton.util.datafix.fixes.name

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class RenameAttributesFix(outputSchema: Schema) : DataFix(outputSchema, false) {

    override fun makeRule(): TypeRewriteRule {
        val stackType = inputSchema.getType(References.ITEM_STACK)
        val tagFinder = stackType.findField("tag")
        return TypeRewriteRule.seq(
            fixTypeEverywhereTyped("Rename ItemStack Attributes", stackType) { typed -> typed.updateTyped(tagFinder) { it.fixItemStackTag() } },
            fixTypeEverywhereTyped("Rename Entity Attributes", inputSchema.getType(References.ENTITY)) { it.fixEntity() },
            fixTypeEverywhereTyped("Rename Player Attributes", inputSchema.getType(References.PLAYER)) { it.fixEntity() }
        )
    }

    private fun Dynamic<*>.fixName() = run {
        val rename = asString().result().map { RENAME_MAP.getOrDefault(it, it) }
        rename.map(::createString).orElse(this)
    }

    private fun Typed<*>.fixItemStackTag() = update(remainderFinder()) { stack ->
        stack.update("AttributeModifiers") { modifiers ->
            val renamed = modifiers.asStreamOpt().result()
                .map { modifierStream -> modifierStream.map { modifier -> modifier.update("AttributeName") { it.fixName() } } }
            renamed.map(modifiers::createList).orElse(modifiers)
        }
    }

    private fun Typed<*>.fixEntity() = update(remainderFinder()) { entity ->
        entity.update("Attributes") { attributes ->
            val renamed = attributes.asStreamOpt().result()
                .map { attributeStream -> attributeStream.map { attribute -> attribute.update("Name") { it.fixName() } } }
            renamed.map(attributes::createList).orElse(attributes)
        }
    }

    companion object {

        private val RENAME_MAP = mapOf(
            "generic.maxHealth" to "generic.max_health",
            "Max Health" to "generic.max_health",
            "zombie.spawnReinforcements" to "zombie.spawn_reinforcements",
            "Spawn Reinforcements Chance" to "zombie.spawn_reinforcements",
            "horse.jumpStrength" to "horse.jump_strength",
            "Jump Strength" to "horse.jump_strength",
            "generic.followRange" to "generic.follow_range",
            "Follow Range" to "generic.follow_range",
            "generic.knockbackResistance" to "generic.knockback_resistance",
            "Knockback Resistance" to "generic.knockback_resistance",
            "generic.movementSpeed" to "generic.movement_speed",
            "Movement Speed" to "generic.movement_speed",
            "generic.flyingSpeed" to "generic.flying_speed",
            "Flying Speed" to "generic.flying_speed",
            "generic.attackDamage" to "generic.attack_damage",
            "generic.attackKnockback" to "generic.attack_knockback",
            "generic.attackSpeed" to "generic.attack_speed",
            "generic.armorToughness" to "generic.armor_toughness"
        )
    }
}

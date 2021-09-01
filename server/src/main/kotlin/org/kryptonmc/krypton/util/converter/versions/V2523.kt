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
package org.kryptonmc.krypton.util.converter.versions

import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.StringDataConverter
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V2523 {

    private const val VERSION = MCVersions.V20W13B + 2
    private val RENAMES = mapOf(
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
        "generic.armorToughness" to "generic.armor_toughness",
    )

    fun register() {
        val entityConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                val attributes = data.getList("Attributes", ObjectType.MAP) ?: return null
                for (i in 0 until attributes.size()) {
                    attributes.getMap<String>(i).updateName("Name")
                }
                return null
            }
        }

        MCTypeRegistry.ENTITY.addStructureConverter(entityConverter)
        MCTypeRegistry.PLAYER.addStructureConverter(entityConverter)

        MCTypeRegistry.ITEM_STACK.addStructureConverter(VERSION) { data, _, _ ->
            val attributes = data.getList("AttributeModifiers", ObjectType.MAP) ?: return@addStructureConverter null
            for (i in 0 until attributes.size()) {
                attributes.getMap<String>(i).updateName("AttributeName")
            }
            null
        }
    }

    private fun MapType<String>.updateName(path: String) {
        getString(path)?.let { name -> RENAMES[name]?.let { setString(path, it) } }
    }
}

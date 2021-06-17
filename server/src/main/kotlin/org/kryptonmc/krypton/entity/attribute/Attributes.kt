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
package org.kryptonmc.krypton.entity.attribute

import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries

object Attributes {

    val MAX_HEALTH = register("generic.max_health", BoundedAttribute("attribute.name.generic.max_health", 20.0, 1.0, 1024.0).syncable())
    val FOLLOW_RANGE = register("generic.follow_range", BoundedAttribute("attribute.name.generic.follow_range", 32.0, 0.0, 2048.0))
    val KNOCKBACK_RESISTANCE = register("generic.knockback_resistance", BoundedAttribute("attribute.name.generic.knockback_resistance", 0.0, 0.0, 1.0))
    val MOVEMENT_SPEED = register("generic.movement_speed", BoundedAttribute("attribute.name.generic.movement_speed", 0.7, 0.0, 1024.0).syncable())
    val FLYING_SPEED = register("generic.flying_speed", BoundedAttribute("attribute.name.generic.flying_speed", 0.4, 0.0, 1024.0).syncable())
    val ATTACK_DAMAGE = register("generic.attack_damage", BoundedAttribute("attribute.name.generic.attack_damage", 2.0, 0.0, 2048.0))
    val ATTACK_KNOCKBACK = register("generic.attack_knockback", BoundedAttribute("attribute.name.generic.attack_knockback", 0.0, 0.0, 5.0))
    val ATTACK_SPEED = register("generic.attack_speed", BoundedAttribute("attribute.name.generic.attack_speed", 4.0, 0.0, 1024.0).syncable())
    val ARMOR = register("generic.armor", BoundedAttribute("attribute.name.generic.armor", 0.0, 0.0, 30.0).syncable())
    val ARMOR_TOUGHNESS = register("generic.armor_toughness", BoundedAttribute("attribute.name.generic.armor_toughness", 0.0, 0.0, 20.0).syncable())
    val LUCK = register("generic.luck", BoundedAttribute("attribute.name.generic.luck", 0.0, -1024.0, 1024.0).syncable())
    val SPAWN_REINFORCEMENTS_CHANCE = register("zombie.spawn_reinforcements", BoundedAttribute("attribute.name.zombie.spawn_reinforcements", 0.0, 0.0, 1.0))
    val JUMP_STRENGTH = register("horse.jump_strength", BoundedAttribute("attribute.name.horse.jump_strength", 0.7, 0.0, 2.0).syncable())

    private fun register(key: String, attribute: Attribute): Attribute = Registries.register(InternalRegistries.ATTRIBUTE, key, attribute)
}

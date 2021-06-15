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

object Attributes {

    val MAX_HEALTH = BoundedAttribute("attribute.name.generic.max_health", 20.0, 1.0, 1024.0).syncable()
    val FOLLOW_RANGE = BoundedAttribute("attribute.name.generic.follow_range", 32.0, 0.0, 2048.0)
    val KNOCKBACK_RESISTANCE = BoundedAttribute("attribute.name.generic.knockback_resistance", 0.0, 0.0, 1.0)
    val MOVEMENT_SPEED = BoundedAttribute("attribute.name.generic.movement_speed", 0.7, 0.0, 1024.0).syncable()
    val FLYING_SPEED = BoundedAttribute("attribute.name.generic.flying_speed", 0.4, 0.0, 1024.0).syncable()
    val ATTACK_DAMAGE = BoundedAttribute("attribute.name.generic.attack_damage", 2.0, 0.0, 2048.0)
    val ATTACK_KNOCKBACK = BoundedAttribute("attribute.name.generic.attack_knockback", 0.0, 0.0, 5.0)
    val ATTACK_SPEED = BoundedAttribute("attribute.name.generic.attack_speed", 4.0, 0.0, 1024.0).syncable()
    val ARMOR = BoundedAttribute("attribute.name.generic.armor", 0.0, 0.0, 30.0).syncable()
    val ARMOR_TOUGHNESS = BoundedAttribute("attribute.name.generic.armor_toughness", 0.0, 0.0, 20.0).syncable()
    val LUCK = BoundedAttribute("attribute.name.generic.luck", 0.0, -1024.0, 1024.0).syncable()
    val SPAWN_REINFORCEMENTS_CHANCE = BoundedAttribute("attribute.name.zombie.spawn_reinforcements", 0.0, 0.0, 1.0)
    val JUMP_STRENGTH = BoundedAttribute("attribute.name.horse.jump_strength", 0.7, 0.0, 2.0).syncable()
}

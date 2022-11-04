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
package org.kryptonmc.krypton.entity.attribute

import org.kryptonmc.api.registry.Registries

object KryptonAttributeTypes {

    @JvmField
    val MAX_HEALTH: KryptonAttributeType = register("generic.max_health", 20.0, 1.0, 1024.0, true)
    @JvmField
    val FOLLOW_RANGE: KryptonAttributeType = register("generic.follow_range", 32.0, 0.0, 2048.0, false)
    @JvmField
    val KNOCKBACK_RESISTANCE: KryptonAttributeType = register("generic.knockback_resistance", 0.0, 0.0, 1.0, false)
    @JvmField
    val MOVEMENT_SPEED: KryptonAttributeType = register("generic.movement_speed", 0.7F.toDouble(), 0.0, 1024.0, true)
    @JvmField
    val FLYING_SPEED: KryptonAttributeType = register("generic.flying_speed", 0.4F.toDouble(), 0.0, 1024.0, true)
    @JvmField
    val ATTACK_DAMAGE: KryptonAttributeType = register("generic.attack_damage", 2.0, 0.0, 2048.0, false)
    @JvmField
    val ATTACK_KNOCKBACK: KryptonAttributeType = register("generic.attack_knockback", 0.0, 0.0, 5.0, false)
    @JvmField
    val ATTACK_SPEED: KryptonAttributeType = register("generic.attack_speed", 4.0, 0.0, 1024.0, true)
    @JvmField
    val ARMOR: KryptonAttributeType = register("generic.armor", 0.0, 0.0, 30.0, true)
    @JvmField
    val ARMOR_TOUGHNESS: KryptonAttributeType = register("generic.armor_toughness", 0.0, 0.0, 20.0, true)
    @JvmField
    val LUCK: KryptonAttributeType = register("generic.luck", 0.0, -1024.0, 1024.0, true)
    @JvmField
    val SPAWN_REINFORCEMENTS_CHANCE: KryptonAttributeType = register("zombie.spawn_reinforcements", 0.0, 0.0, 1.0, false)
    @JvmField
    val JUMP_STRENGTH: KryptonAttributeType = register("horse.jump_strength", 0.7, 0.0, 2.0, true)

    @JvmStatic
    private fun register(name: String, default: Double, min: Double, max: Double, sendToClient: Boolean): KryptonAttributeType =
        Registries.ATTRIBUTE.register(name, KryptonRangedAttributeType(default, sendToClient, "attribute.name.$name", min, max))
}

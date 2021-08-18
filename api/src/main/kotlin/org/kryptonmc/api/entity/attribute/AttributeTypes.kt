/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

/**
 * All of the built-in attribute types.
 */
object AttributeTypes {

    // @formatter:off
    @JvmField val MAX_HEALTH = register("generic.max_health", true, 20.0, 0.0, 1024.0)
    @JvmField val FOLLOW_RANGE = register("generic.follow_range", false, 32.0, 0.0, 2048.0)
    @JvmField val KNOCKBACK_RESISTANCE = register("generic.knockback_resistance", false, 0.0, 0.0, 1.0)
    @JvmField val MOVEMENT_SPEED = register("generic.movement_speed", true, 0.7, 0.0, 1024.0)
    @JvmField val FLYING_SPEED = register("generic.flying_speed", true, 0.4, 0.0, 1024.0)
    @JvmField val ATTACK_DAMAGE = register("generic.attack_damage", false, 2.0, 0.0, 2048.0)
    @JvmField val ATTACK_KNOCKBACK = register("generic.attack_knockback", false, 0.0, 0.0, 5.0)
    @JvmField val ATTACK_SPEED = register("generic.attack_speed", true, 4.0, 0.0, 1024.0)
    @JvmField val ARMOR = register("generic.armor", true, 0.0, 0.0, 30.0)
    @JvmField val ARMOR_TOUGHNESS = register("generic.armor_toughness", true, 0.0, 0.0, 20.0)
    @JvmField val LUCK = register("generic.luck", true, 0.0, -1024.0, 1024.0)
    @JvmField val SPAWN_REINFORCEMENTS = register("zombie.spawn_reinforcements", false, 0.0, 0.0, 1.0)
    @JvmField val JUMP_STRENGTH = register("horse.jump_strength", true, 0.7, 0.0, 2.0)

    // @formatter:on
    private fun register(name: String, sendToClient: Boolean, baseDefault: Double, minimum: Double, maximum: Double): AttributeType {
        val key = Key.key(name)
        return Registries.register(Registries.ATTRIBUTE, key, AttributeType(key, sendToClient, baseDefault, minimum, maximum))
    }
}

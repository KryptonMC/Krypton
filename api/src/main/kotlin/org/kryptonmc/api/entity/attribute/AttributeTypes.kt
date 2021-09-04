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
@Suppress("UndocumentedPublicProperty")
public object AttributeTypes {

    // @formatter:off
    @JvmField public val MAX_HEALTH: AttributeType =
        register("generic.max_health", true, 20.0, 0.0, 1024.0)
    @JvmField public val FOLLOW_RANGE: AttributeType =
        register("generic.follow_range", false, 32.0, 0.0, 2048.0)
    @JvmField public val KNOCKBACK_RESISTANCE: AttributeType =
        register("generic.knockback_resistance", false, 0.0, 0.0, 1.0)
    @JvmField public val MOVEMENT_SPEED: AttributeType =
        register("generic.movement_speed", true, 0.7, 0.0, 1024.0)
    @JvmField public val FLYING_SPEED: AttributeType =
        register("generic.flying_speed", true, 0.4, 0.0, 1024.0)
    @JvmField public val ATTACK_DAMAGE: AttributeType =
        register("generic.attack_damage", false, 2.0, 0.0, 2048.0)
    @JvmField public val ATTACK_KNOCKBACK: AttributeType =
        register("generic.attack_knockback", false, 0.0, 0.0, 5.0)
    @JvmField public val ATTACK_SPEED: AttributeType =
        register("generic.attack_speed", true, 4.0, 0.0, 1024.0)
    @JvmField public val ARMOR: AttributeType =
        register("generic.armor", true, 0.0, 0.0, 30.0)
    @JvmField public val ARMOR_TOUGHNESS: AttributeType =
        register("generic.armor_toughness", true, 0.0, 0.0, 20.0)
    @JvmField public val LUCK: AttributeType =
        register("generic.luck", true, 0.0, -1024.0, 1024.0)
    @JvmField public val SPAWN_REINFORCEMENTS: AttributeType =
        register("zombie.spawn_reinforcements", false, 0.0, 0.0, 1.0)
    @JvmField public val JUMP_STRENGTH: AttributeType =
        register("horse.jump_strength", true, 0.7, 0.0, 2.0)

    // @formatter:on
    private fun register(name: String, sendToClient: Boolean, baseDefault: Double, minimum: Double, maximum: Double): AttributeType {
        val key = Key.key(name)
        return Registries.register(Registries.ATTRIBUTE, key, AttributeType(key, sendToClient, baseDefault, minimum, maximum))
    }
}

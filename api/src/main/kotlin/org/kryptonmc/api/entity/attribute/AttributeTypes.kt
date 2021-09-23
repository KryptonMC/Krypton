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
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in attribute types.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(AttributeType::class)
public object AttributeTypes {

    // @formatter:off
    @JvmField public val MAX_HEALTH: AttributeType = get("generic.max_health")
    @JvmField public val FOLLOW_RANGE: AttributeType = get("generic.follow_range")
    @JvmField public val KNOCKBACK_RESISTANCE: AttributeType = get("generic.knockback_resistance")
    @JvmField public val MOVEMENT_SPEED: AttributeType = get("generic.movement_speed")
    @JvmField public val FLYING_SPEED: AttributeType = get("generic.flying_speed")
    @JvmField public val ATTACK_DAMAGE: AttributeType = get("generic.attack_damage")
    @JvmField public val ATTACK_KNOCKBACK: AttributeType = get("generic.attack_knockback")
    @JvmField public val ATTACK_SPEED: AttributeType = get("generic.attack_speed")
    @JvmField public val ARMOR: AttributeType = get("generic.armor")
    @JvmField public val ARMOR_TOUGHNESS: AttributeType = get("generic.armor_toughness")
    @JvmField public val LUCK: AttributeType = get("generic.luck")
    @JvmField public val SPAWN_REINFORCEMENTS: AttributeType = get("zombie.spawn_reinforcements")
    @JvmField public val JUMP_STRENGTH: AttributeType = get("horse.jump_strength")

    // @formatter:on
    private fun get(name: String): AttributeType = Registries.ATTRIBUTE[Key.key(name)]!!
}

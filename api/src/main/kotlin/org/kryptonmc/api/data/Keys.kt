/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.data

import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.PushReaction
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.util.Catalogue
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d

/**
 * All of the keys for every data holder in the API.
 */
@Catalogue(Key::class)
public object Keys {

    @JvmField
    public val HARDNESS: Key<Double> = of("hardness")
    @JvmField
    public val EXPLOSION_RESISTANCE: Key<Double> = of("explosion_resistance")
    @JvmField
    public val FRICTION: Key<Double> = of("friction")
    @JvmField
    public val IS_SOLID: Key<Boolean> = of("is_solid")
    @JvmField
    public val IS_LIQUID: Key<Boolean> = of("is_liquid")
    @JvmField
    public val IS_FLAMMABLE: Key<Boolean> = of("is_flammable")
    @JvmField
    public val IS_REPLACEABLE: Key<Boolean> = of("is_replaceable")
    @JvmField
    public val IS_OPAQUE: Key<Boolean> = of("is_opaque")
    @JvmField
    public val BLOCKS_MOTION: Key<Boolean> = of("blocks_motion")
    @JvmField
    public val HAS_GRAVITY: Key<Boolean> = of("has_gravity")
    @JvmField
    public val CAN_RESPAWN_IN: Key<Boolean> = of("can_respawn_in")
    @JvmField
    public val PUSH_REACTION: Key<PushReaction> = of("push_reaction")
    @JvmField
    public val HAS_BLOCK_ENTITY: Key<Boolean> = of("has_block_entity")

    @JvmField
    public val CUSTOM_NAME: Key<Component> = of("custom_name")
    @JvmField
    public val IS_CUSTOM_NAME_VISIBLE: Key<Boolean> = of("is_custom_name_visible")
    @JvmField
    public val DISPLAY_NAME: Key<Component> = of("display_name")
    @JvmField
    public val LOCATION: Key<Vector3d> = of("location")
    @JvmField
    public val ROTATION: Key<Vector2f> = of("rotation")
    @JvmField
    public val VELOCITY: Key<Vector3d> = of("velocity")
    @JvmField
    public val PASSENGERS: Key<List<Entity>> = of("passengers")
    @JvmField
    public val VEHICLE: Key<Entity> = of("vehicle")
    @JvmField
    public val IS_INVULNERABLE: Key<Boolean> = of("is_invulnerable")
    @JvmField
    public val IS_ON_FIRE: Key<Boolean> = of("is_on_fire")
    @JvmField
    public val IS_ON_GROUND: Key<Boolean> = of("is_on_ground")
    @JvmField
    public val IS_SNEAKING: Key<Boolean> = of("is_sneaking")
    @JvmField
    public val IS_SPRINTING: Key<Boolean> = of("is_sprinting")
    @JvmField
    public val IS_SWIMMING: Key<Boolean> = of("is_swimming")
    @JvmField
    public val IS_INVISIBLE: Key<Boolean> = of("is_invisible")
    @JvmField
    public val IS_GLOWING: Key<Boolean> = of("is_glowing")
    @JvmField
    public val IS_GLIDING: Key<Boolean> = of("is_gliding")
    @JvmField
    public val IS_SILENT: Key<Boolean> = of("is_silent")
    @JvmField
    public val TICKS_EXISTED: Key<Int> = of("ticks_existed")
    @JvmField
    public val AIR: Key<Int> = of("air")
    @JvmField
    public val FIRE_TICKS: Key<Int> = of("fire_ticks")
    @JvmField
    public val FROZEN_TICKS: Key<Int> = of("frozen_ticks")
    @JvmField
    public val FALL_DISTANCE: Key<Float> = of("fall_distance")
    @JvmField
    public val IN_WATER: Key<Boolean> = of("in_water")
    @JvmField
    public val IN_LAVA: Key<Boolean> = of("in_lava")
    @JvmField
    public val UNDERWATER: Key<Boolean> = of("underwater")

    @JvmStatic
    private inline fun <reified V> of(name: String): Key<V> = Key.of(net.kyori.adventure.key.Key.key("krypton", name))
}

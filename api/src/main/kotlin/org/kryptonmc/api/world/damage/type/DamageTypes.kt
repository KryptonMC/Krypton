/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.damage.type

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla damage sources.
 */
@Catalogue(DamageType::class)
public object DamageTypes {

    @JvmField public val IN_FIRE: DamageType = get("in_fire")
    @JvmField public val LIGHTNING_BOLT: DamageType = get("lightning_bolt")
    @JvmField public val ON_FIRE: DamageType = get("on_fire")
    @JvmField public val LAVA: DamageType = get("lava")
    @JvmField public val HOT_FLOOR: DamageType = get("hot_floor")
    @JvmField public val SUFFOCATION: DamageType = get("suffocation")
    @JvmField public val CRAMMING: DamageType = get("cramming")
    @JvmField public val DROWNING: DamageType = get("drowning")
    @JvmField public val STARVING: DamageType = get("starving")
    @JvmField public val CACTUS: DamageType = get("cactus")
    @JvmField public val FALL: DamageType = get("falling")
    @JvmField public val FLY_INTO_WALL: DamageType = get("fly_into_wall")
    @JvmField public val VOID: DamageType = get("void")
    @JvmField public val GENERIC: DamageType = get("generic")
    @JvmField public val MAGIC: DamageType = get("magic")
    @JvmField public val WITHER: DamageType = get("wither")
    @JvmField public val ANVIL: DamageType = get("anvil")
    @JvmField public val FALLING_BLOCK: DamageType = get("falling_block")
    @JvmField public val DRAGON_BREATH: DamageType = get("dragon_breath")
    @JvmField public val DRY_OUT: DamageType = get("dry_out")
    @JvmField public val SWEET_BERRY_BUSH: DamageType = get("sweet_berry_bush")
    @JvmField public val FREEZING: DamageType = get("freezing")
    @JvmField public val FALLING_STALACTITE: DamageType = get("falling_stalactite")
    @JvmField public val STALAGMITE: DamageType = get("stalagmite")
    @JvmField public val STING: DamageType = get("sting")
    @JvmField public val GENERIC_MOB_ATTACK: DamageType = get("generic_mob_attack")
    @JvmField public val PASSIVE_MOB_ATTACK: DamageType = get("passive_mob_attack")
    @JvmField public val PROJECTILE_MOB_ATTACK: DamageType = get("indirect_mob_attack")
    @JvmField public val PLAYER_ATTACK: DamageType = get("player_attack")
    @JvmField public val ARROW: DamageType = get("arrow")
    @JvmField public val TRIDENT: DamageType = get("trident")
    @JvmField public val FIREWORKS: DamageType = get("fireworks")
    @JvmField public val FIREBALL: DamageType = get("fireball")
    @JvmField public val FIREBALL_ON_FIRE: DamageType = get("fireball_on_fire")
    @JvmField public val WITHER_SKULL: DamageType = get("wither_skull")
    @JvmField public val THROWN_PROJECTILE: DamageType = get("thrown_projectile")
    @JvmField public val INDIRECT_MAGIC: DamageType = get("indirect_magic")
    @JvmField public val THORNS: DamageType = get("thorns")
    @JvmField public val EXPLOSION: DamageType = get("explosion")
    @JvmField public val PLAYER_EXPLOSION: DamageType = get("player_explosion")
    @JvmField public val BAD_RESPAWN_POINT: DamageType = get("bad_respawn_point")

    @JvmStatic
    private fun get(name: String): DamageType = Registries.DAMAGE_TYPES[Key.key(name)]!!
}

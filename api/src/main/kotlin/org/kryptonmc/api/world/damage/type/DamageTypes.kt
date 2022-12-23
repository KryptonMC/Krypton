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
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All of the built-in vanilla damage sources.
 */
@Catalogue(DamageType::class)
public object DamageTypes {

    @JvmField
    public val IN_FIRE: RegistryReference<DamageType> = of("in_fire")
    @JvmField
    public val LIGHTNING_BOLT: RegistryReference<DamageType> = of("lightning_bolt")
    @JvmField
    public val ON_FIRE: RegistryReference<DamageType> = of("on_fire")
    @JvmField
    public val LAVA: RegistryReference<DamageType> = of("lava")
    @JvmField
    public val HOT_FLOOR: RegistryReference<DamageType> = of("hot_floor")
    @JvmField
    public val SUFFOCATION: RegistryReference<DamageType> = of("suffocation")
    @JvmField
    public val CRAMMING: RegistryReference<DamageType> = of("cramming")
    @JvmField
    public val DROWNING: RegistryReference<DamageType> = of("drowning")
    @JvmField
    public val STARVING: RegistryReference<DamageType> = of("starving")
    @JvmField
    public val CACTUS: RegistryReference<DamageType> = of("cactus")
    @JvmField
    public val FALL: RegistryReference<DamageType> = of("falling")
    @JvmField
    public val FLY_INTO_WALL: RegistryReference<DamageType> = of("fly_into_wall")
    @JvmField
    public val VOID: RegistryReference<DamageType> = of("void")
    @JvmField
    public val GENERIC: RegistryReference<DamageType> = of("generic")
    @JvmField
    public val MAGIC: RegistryReference<DamageType> = of("magic")
    @JvmField
    public val WITHER: RegistryReference<DamageType> = of("wither")
    @JvmField
    public val ANVIL: RegistryReference<DamageType> = of("anvil")
    @JvmField
    public val FALLING_BLOCK: RegistryReference<DamageType> = of("falling_block")
    @JvmField
    public val DRAGON_BREATH: RegistryReference<DamageType> = of("dragon_breath")
    @JvmField
    public val DRY_OUT: RegistryReference<DamageType> = of("dry_out")
    @JvmField
    public val SWEET_BERRY_BUSH: RegistryReference<DamageType> = of("sweet_berry_bush")
    @JvmField
    public val FREEZING: RegistryReference<DamageType> = of("freezing")
    @JvmField
    public val FALLING_STALACTITE: RegistryReference<DamageType> = of("falling_stalactite")
    @JvmField
    public val STALAGMITE: RegistryReference<DamageType> = of("stalagmite")
    @JvmField
    public val STING: RegistryReference<DamageType> = of("sting")
    @JvmField
    public val GENERIC_MOB_ATTACK: RegistryReference<DamageType> = of("generic_mob_attack")
    @JvmField
    public val PASSIVE_MOB_ATTACK: RegistryReference<DamageType> = of("passive_mob_attack")
    @JvmField
    public val PROJECTILE_MOB_ATTACK: RegistryReference<DamageType> = of("indirect_mob_attack")
    @JvmField
    public val PLAYER_ATTACK: RegistryReference<DamageType> = of("player_attack")
    @JvmField
    public val ARROW: RegistryReference<DamageType> = of("arrow")
    @JvmField
    public val TRIDENT: RegistryReference<DamageType> = of("trident")
    @JvmField
    public val FIREWORKS: RegistryReference<DamageType> = of("fireworks")
    @JvmField
    public val FIREBALL: RegistryReference<DamageType> = of("fireball")
    @JvmField
    public val FIREBALL_ON_FIRE: RegistryReference<DamageType> = of("fireball_on_fire")
    @JvmField
    public val WITHER_SKULL: RegistryReference<DamageType> = of("wither_skull")
    @JvmField
    public val THROWN_PROJECTILE: RegistryReference<DamageType> = of("thrown_projectile")
    @JvmField
    public val INDIRECT_MAGIC: RegistryReference<DamageType> = of("indirect_magic")
    @JvmField
    public val THORNS: RegistryReference<DamageType> = of("thorns")
    @JvmField
    public val EXPLOSION: RegistryReference<DamageType> = of("explosion")
    @JvmField
    public val PLAYER_EXPLOSION: RegistryReference<DamageType> = of("player_explosion")
    @JvmField
    public val SONIC_BOOM: RegistryReference<DamageType> = of("sonic_boom")
    @JvmField
    public val BAD_RESPAWN_POINT: RegistryReference<DamageType> = of("bad_respawn_point")

    @JvmStatic
    private fun of(name: String): RegistryReference<DamageType> = RegistryReference.of(Registries.DAMAGE_TYPES, Key.key(name))
}

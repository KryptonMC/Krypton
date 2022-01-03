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
package org.kryptonmc.krypton.world.damage.type

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.damage.type.DamageType

object KryptonDamageTypes {

    @JvmField val IN_FIRE: DamageType = register("in_fire", "inFire") {
        bypassesArmor()
        fire()
    }
    @JvmField val LIGHTNING_BOLT: DamageType = register("lightning_bolt", "lightningBolt")
    @JvmField val ON_FIRE: DamageType = register("on_fire", "onFire") {
        bypassesArmor()
        fire()
    }
    @JvmField val LAVA: DamageType = register("lava", "lava") { fire() }
    @JvmField val HOT_FLOOR: DamageType = register("hot_floor", "hotFloor") { fire() }
    @JvmField val SUFFOCATION: DamageType = register("suffocation", "inWall") { bypassesArmor() }
    @JvmField val CRAMMING: DamageType = register("cramming", "cramming") { bypassesArmor() }
    @JvmField val DROWNING: DamageType = register("drowning", "drown") { bypassesArmor() }
    @JvmField val STARVING: DamageType = register("starving", "starve") {
        bypassesArmor()
        bypassesMagic()
    }
    @JvmField val CACTUS: DamageType = register("cactus", "cactus")
    @JvmField val FALL: DamageType = register("falling", "fall") {
        bypassesArmor()
        fall()
    }
    @JvmField val FLY_INTO_WALL: DamageType = register("fly_into_wall", "flyIntoWall") { bypassesArmor() }
    @JvmField val VOID: DamageType = register("void", "outOfWorld") {
        bypassesArmor()
        bypassesInvulnerability()
    }
    @JvmField val GENERIC: DamageType = register("generic", "generic") { bypassesArmor() }
    @JvmField val MAGIC: DamageType = register("magic", "magic") {
        bypassesArmor()
        magic()
    }
    @JvmField val WITHER: DamageType = register("wither", "wither") { bypassesArmor() }
    @JvmField val ANVIL: DamageType = register("anvil", "anvil") { damagesHelmet() }
    @JvmField val FALLING_BLOCK: DamageType = register("falling_block", "fallingBlock") { damagesHelmet() }
    @JvmField val DRAGON_BREATH: DamageType = register("dragon_breath", "dragonBreath") { bypassesArmor() }
    @JvmField val DRY_OUT: DamageType = register("dry_out", "dryout")
    @JvmField val SWEET_BERRY_BUSH: DamageType = register("sweet_berry_bush", "sweetBerryBush")
    @JvmField val FREEZING: DamageType = register("freezing", "freeze") { bypassesArmor() }
    @JvmField val FALLING_STALACTITE: DamageType = register("falling_stalactite", "fallingStalactite") { damagesHelmet() }
    @JvmField val STALAGMITE: DamageType = register("stalagmite", "stalagmite") { fall() }
    @JvmField val STING: DamageType = register("sting", "sting")
    @JvmField val GENERIC_MOB_ATTACK: DamageType = register("generic_mob_attack", "mob")
    @JvmField val PASSIVE_MOB_ATTACK: DamageType = register("passive_mob_attack", "mob") { aggravatesTarget(false) }
    @JvmField val PROJECTILE_MOB_ATTACK: DamageType = register("indirect_mob_attack", "mob") { projectile() }
    @JvmField val PLAYER_ATTACK: DamageType = register("player_attack", "player")
    @JvmField val ARROW: DamageType = register("arrow", "arrow") { projectile() }
    @JvmField val TRIDENT: DamageType = register("trident", "trident") { projectile() }
    @JvmField val FIREWORKS: DamageType = register("fireworks", "fireworks") { explosion() }
    @JvmField val FIREBALL: DamageType = register("fireball", "fireball") {
        fire()
        projectile()
    }
    @JvmField val FIREBALL_ON_FIRE: DamageType = register("fireball_on_fire", "onFire") {
        fire()
        projectile()
    }
    @JvmField val WITHER_SKULL: DamageType = register("wither_skull", "witherSkull") { projectile() }
    @JvmField val THROWN_PROJECTILE: DamageType = register("thrown_projectile", "thrown") { projectile() }
    @JvmField val INDIRECT_MAGIC: DamageType = register("indirect_magic", "indirectMagic") {
        bypassesArmor()
        magic()
    }
    @JvmField val THORNS: DamageType = register("thorns", "thorns") {
        thorns()
        magic()
    }
    @JvmField val EXPLOSION: DamageType = register("explosion", "explosion") {
        scalesWithDifficulty()
        explosion()
    }
    @JvmField val PLAYER_EXPLOSION: DamageType = register("player_explosion", "explosion.player") {
        scalesWithDifficulty()
        explosion()
    }
    @JvmField val BAD_RESPAWN_POINT: DamageType = register("bad_respawn_point", "badRespawnPoint") {
        scalesWithDifficulty()
        explosion()
    }

    @JvmStatic
    private fun register(name: String, messageId: String, builder: KryptonDamageType.Builder.() -> Unit = {}): DamageType {
        val key = Key.key(name)
        return Registries.DAMAGE_TYPES.register(key, KryptonDamageType.Builder(key, messageId).apply(builder).build())
    }
}

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
package org.kryptonmc.krypton.world.damage.type

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.damage.type.DamageType

object KryptonDamageTypes {

    @JvmField val IN_FIRE = register("in_fire", "inFire") {
        bypassesArmor()
        fire()
    }
    @JvmField val LIGHTNING_BOLT = register("lightning_bolt", "lightningBolt")
    @JvmField val ON_FIRE = register("on_fire", "onFire") {
        bypassesArmor()
        fire()
    }
    @JvmField val LAVA = register("lava", "lava") { fire() }
    @JvmField val HOT_FLOOR = register("hot_floor", "hotFloor") { fire() }
    @JvmField val SUFFOCATION = register("suffocation", "inWall") { bypassesArmor() }
    @JvmField val CRAMMING = register("cramming", "cramming") { bypassesArmor() }
    @JvmField val DROWNING = register("drowning", "drown") { bypassesArmor() }
    @JvmField val STARVING = register("starving", "starve") {
        bypassesArmor()
        bypassesMagic()
    }
    @JvmField val CACTUS = register("cactus", "cactus")
    @JvmField val FALL = register("falling", "fall") {
        bypassesArmor()
        fall()
    }
    @JvmField val FLY_INTO_WALL = register("fly_into_wall", "flyIntoWall") { bypassesArmor() }
    @JvmField val VOID = register("void", "outOfWorld") {
        bypassesArmor()
        bypassesInvulnerability()
    }
    @JvmField val GENERIC = register("generic", "generic") { bypassesArmor() }
    @JvmField val MAGIC = register("magic", "magic") {
        bypassesArmor()
        magic()
    }
    @JvmField val WITHER = register("wither", "wither") { bypassesArmor() }
    @JvmField val ANVIL = register("anvil", "anvil") { damagesHelmet() }
    @JvmField val FALLING_BLOCK = register("falling_block", "fallingBlock") { damagesHelmet() }
    @JvmField val DRAGON_BREATH = register("dragon_breath", "dragonBreath") { bypassesArmor() }
    @JvmField val DRY_OUT = register("dry_out", "dryout")
    @JvmField val SWEET_BERRY_BUSH = register("sweet_berry_bush", "sweetBerryBush")
    @JvmField val FREEZING = register("freezing", "freeze") { bypassesArmor() }
    @JvmField val FALLING_STALACTITE = register("falling_stalactite", "fallingStalactite") { damagesHelmet() }
    @JvmField val STALAGMITE = register("stalagmite", "stalagmite") { fall() }
    @JvmField val STING = register("sting", "sting")
    @JvmField val GENERIC_MOB_ATTACK = register("generic_mob_attack", "mob")
    @JvmField val PASSIVE_MOB_ATTACK = register("passive_mob_attack", "mob") { aggravatesTarget(false) }
    @JvmField val PROJECTILE_MOB_ATTACK = register("indirect_mob_attack", "mob") { projectile() }
    @JvmField val PLAYER_ATTACK = register("player_attack", "player")
    @JvmField val ARROW = register("arrow", "arrow") { projectile() }
    @JvmField val TRIDENT = register("trident", "trident") { projectile() }
    @JvmField val FIREWORKS = register("fireworks", "fireworks") { explosion() }
    @JvmField val FIREBALL = register("fireball", "fireball") {
        fire()
        projectile()
    }
    @JvmField val FIREBALL_ON_FIRE = register("fireball_on_fire", "onFire") {
        fire()
        projectile()
    }
    @JvmField val WITHER_SKULL = register("wither_skull", "witherSkull") { projectile() }
    @JvmField val THROWN_PROJECTILE = register("thrown_projectile", "thrown") { projectile() }
    @JvmField val INDIRECT_MAGIC = register("indirect_magic", "indirectMagic") {
        bypassesArmor()
        magic()
    }
    @JvmField val THORNS = register("thorns", "thorns") {
        thorns()
        magic()
    }
    @JvmField val EXPLOSION = register("explosion", "explosion") {
        scalesWithDifficulty()
        explosion()
    }
    @JvmField val PLAYER_EXPLOSION = register("player_explosion", "explosion.player") {
        scalesWithDifficulty()
        explosion()
    }
    @JvmField val BAD_RESPAWN_POINT = register("bad_respawn_point", "badRespawnPoint") {
        scalesWithDifficulty()
        explosion()
    }

    @JvmStatic
    private fun register(name: String, messageId: String, builder: KryptonDamageType.Builder.() -> Unit = {}): DamageType {
        val key = Key.key(name)
        return Registries.DAMAGE_SOURCES.register(key, KryptonDamageType.Builder(key, messageId).apply(builder).build())
    }
}

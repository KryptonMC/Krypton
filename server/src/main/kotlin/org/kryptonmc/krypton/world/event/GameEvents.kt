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
package org.kryptonmc.krypton.world.event

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries

object GameEvents {

    @JvmField val BLOCK_ATTACH = register("block_attach")
    @JvmField val BLOCK_CHANGE = register("block_change")
    @JvmField val BLOCK_CLOSE = register("block_close")
    @JvmField val BLOCK_DESTROY = register("block_destroy")
    @JvmField val BLOCK_DETACH = register("block_detach")
    @JvmField val BLOCK_OPEN = register("block_open")
    @JvmField val BLOCK_PLACE = register("block_place")
    @JvmField val BLOCK_PRESS = register("block_press")
    @JvmField val BLOCK_SWITCH = register("block_switch")
    @JvmField val BLOCK_UNPRESS = register("block_unpress")
    @JvmField val BLOCK_UNSWITCH = register("block_unswitch")
    @JvmField val CONTAINER_CLOSE = register("container_close")
    @JvmField val CONTAINER_OPEN = register("container_open")
    @JvmField val DISPENSE_FAIL = register("dispense_fail")
    @JvmField val DRINKING_FINISH = register("drinking_finish")
    @JvmField val EAT = register("eat")
    @JvmField val ELYTRA_FREE_FALL = register("elytra_free_fall")
    @JvmField val ENTITY_DAMAGED = register("entity_damaged")
    @JvmField val ENTITY_KILLED = register("entity_killed")
    @JvmField val ENTITY_PLACE = register("entity_place")
    @JvmField val EQUIP = register("equip")
    @JvmField val EXPLODE = register("explode")
    @JvmField val FISHING_ROD_CAST = register("fishing_rod_cast")
    @JvmField val FISHING_ROD_REEL_IN = register("fishing_rod_reel_in")
    @JvmField val FLAP = register("flap")
    @JvmField val FLUID_PICKUP = register("fluid_pickup")
    @JvmField val FLUID_PLACE = register("fluid_place")
    @JvmField val HIT_GROUND = register("hit_ground")
    @JvmField val MOB_INTERACT = register("mob_interact")
    @JvmField val LIGHTNING_STRIKE = register("lightning_strike")
    @JvmField val MINECART_MOVING = register("minecart_moving")
    @JvmField val PISTON_CONTRACT = register("piston_contract")
    @JvmField val PISTON_EXTEND = register("piston_extend")
    @JvmField val PRIME_FUSE = register("prime_fuse")
    @JvmField val PROJECTILE_LAND = register("projectile_land")
    @JvmField val PROJECTILE_SHOOT = register("projectile_shoot")
    @JvmField val RAVAGER_ROAR = register("ravager_roar")
    @JvmField val RING_BELL = register("ring_bell")
    @JvmField val SHEAR = register("shear")
    @JvmField val SHULKER_CLOSE = register("shulker_close")
    @JvmField val SHULKER_OPEN = register("shulker_open")
    @JvmField val SPLASH = register("splash")
    @JvmField val STEP = register("step")
    @JvmField val SWIM = register("swim")
    @JvmField val WOLF_SHAKING = register("wolf_shaking")

    @JvmStatic
    private fun register(name: String): GameEvent {
        val key = Key.key(name)
        return InternalRegistries.GAME_EVENT.register(key, GameEvent(key))
    }
}

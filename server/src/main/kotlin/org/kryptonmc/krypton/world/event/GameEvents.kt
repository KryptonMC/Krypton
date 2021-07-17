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

    val BLOCK_ATTACH = register("block_attach")
    val BLOCK_CHANGE = register("block_change")
    val BLOCK_CLOSE = register("block_close")
    val BLOCK_DESTROY = register("block_destroy")
    val BLOCK_DETACH = register("block_detach")
    val BLOCK_OPEN = register("block_open")
    val BLOCK_PLACE = register("block_place")
    val BLOCK_PRESS = register("block_press")
    val BLOCK_SWITCH = register("block_switch")
    val BLOCK_UNPRESS = register("block_unpress")
    val BLOCK_UNSWITCH = register("block_unswitch")
    val CONTAINER_CLOSE = register("container_close")
    val CONTAINER_OPEN = register("container_open")
    val DISPENSE_FAIL = register("dispense_fail")
    val DRINKING_FINISH = register("drinking_finish")
    val EAT = register("eat")
    val ELYTRA_FREE_FALL = register("elytra_free_fall")
    val ENTITY_DAMAGED = register("entity_damaged")
    val ENTITY_KILLED = register("entity_killed")
    val ENTITY_PLACE = register("entity_place")
    val EQUIP = register("equip")
    val EXPLODE = register("explode")
    val FISHING_ROD_CAST = register("fishing_rod_cast")
    val FISHING_ROD_REEL_IN = register("fishing_rod_reel_in")
    val FLAP = register("flap")
    val FLUID_PICKUP = register("fluid_pickup")
    val FLUID_PLACE = register("fluid_place")
    val HIT_GROUND = register("hit_ground")
    val MOB_INTERACT = register("mob_interact")
    val LIGHTNING_STRIKE = register("lightning_strike")
    val MINECART_MOVING = register("minecart_moving")
    val PISTON_CONTRACT = register("piston_contract")
    val PISTON_EXTEND = register("piston_extend")
    val PRIME_FUSE = register("prime_fuse")
    val PROJECTILE_LAND = register("projectile_land")
    val PROJECTILE_SHOOT = register("projectile_shoot")
    val RAVAGER_ROAR = register("ravager_roar")
    val RING_BELL = register("ring_bell")
    val SHEAR = register("shear")
    val SHULKER_CLOSE = register("shulker_close")
    val SHULKER_OPEN = register("shulker_open")
    val SPLASH = register("splash")
    val STEP = register("step")
    val SWIM = register("swim")
    val WOLF_SHAKING = register("wolf_shaking")

    private fun register(name: String) = Registries.register(InternalRegistries.GAME_EVENT, name, GameEvent(Key.key(name)))
}

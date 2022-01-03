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
package org.kryptonmc.krypton.world.event

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries

object GameEvents {

    @JvmField val BLOCK_ATTACH: GameEvent = register("block_attach")
    @JvmField val BLOCK_CHANGE: GameEvent = register("block_change")
    @JvmField val BLOCK_CLOSE: GameEvent = register("block_close")
    @JvmField val BLOCK_DESTROY: GameEvent = register("block_destroy")
    @JvmField val BLOCK_DETACH: GameEvent = register("block_detach")
    @JvmField val BLOCK_OPEN: GameEvent = register("block_open")
    @JvmField val BLOCK_PLACE: GameEvent = register("block_place")
    @JvmField val BLOCK_PRESS: GameEvent = register("block_press")
    @JvmField val BLOCK_SWITCH: GameEvent = register("block_switch")
    @JvmField val BLOCK_UNPRESS: GameEvent = register("block_unpress")
    @JvmField val BLOCK_UNSWITCH: GameEvent = register("block_unswitch")
    @JvmField val CONTAINER_CLOSE: GameEvent = register("container_close")
    @JvmField val CONTAINER_OPEN: GameEvent = register("container_open")
    @JvmField val DISPENSE_FAIL: GameEvent = register("dispense_fail")
    @JvmField val DRINKING_FINISH: GameEvent = register("drinking_finish")
    @JvmField val EAT: GameEvent = register("eat")
    @JvmField val ELYTRA_FREE_FALL: GameEvent = register("elytra_free_fall")
    @JvmField val ENTITY_DAMAGED: GameEvent = register("entity_damaged")
    @JvmField val ENTITY_KILLED: GameEvent = register("entity_killed")
    @JvmField val ENTITY_PLACE: GameEvent = register("entity_place")
    @JvmField val EQUIP: GameEvent = register("equip")
    @JvmField val EXPLODE: GameEvent = register("explode")
    @JvmField val FISHING_ROD_CAST: GameEvent = register("fishing_rod_cast")
    @JvmField val FISHING_ROD_REEL_IN: GameEvent = register("fishing_rod_reel_in")
    @JvmField val FLAP: GameEvent = register("flap")
    @JvmField val FLUID_PICKUP: GameEvent = register("fluid_pickup")
    @JvmField val FLUID_PLACE: GameEvent = register("fluid_place")
    @JvmField val HIT_GROUND: GameEvent = register("hit_ground")
    @JvmField val MOB_INTERACT: GameEvent = register("mob_interact")
    @JvmField val LIGHTNING_STRIKE: GameEvent = register("lightning_strike")
    @JvmField val MINECART_MOVING: GameEvent = register("minecart_moving")
    @JvmField val PISTON_CONTRACT: GameEvent = register("piston_contract")
    @JvmField val PISTON_EXTEND: GameEvent = register("piston_extend")
    @JvmField val PRIME_FUSE: GameEvent = register("prime_fuse")
    @JvmField val PROJECTILE_LAND: GameEvent = register("projectile_land")
    @JvmField val PROJECTILE_SHOOT: GameEvent = register("projectile_shoot")
    @JvmField val RAVAGER_ROAR: GameEvent = register("ravager_roar")
    @JvmField val RING_BELL: GameEvent = register("ring_bell")
    @JvmField val SHEAR: GameEvent = register("shear")
    @JvmField val SHULKER_CLOSE: GameEvent = register("shulker_close")
    @JvmField val SHULKER_OPEN: GameEvent = register("shulker_open")
    @JvmField val SPLASH: GameEvent = register("splash")
    @JvmField val STEP: GameEvent = register("step")
    @JvmField val SWIM: GameEvent = register("swim")
    @JvmField val WOLF_SHAKING: GameEvent = register("wolf_shaking")

    @JvmStatic
    private fun register(name: String): GameEvent {
        val key = Key.key(name)
        return InternalRegistries.GAME_EVENT.register(key, GameEvent(key))
    }
}

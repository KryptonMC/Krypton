/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.gameevent

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.registry.KryptonRegistries

object GameEvents {

    @JvmField
    val BLOCK_ACTIVATE: GameEvent = register("block_activate")
    @JvmField
    val BLOCK_ATTACH: GameEvent = register("block_attach")
    @JvmField
    val BLOCK_CHANGE: GameEvent = register("block_change")
    @JvmField
    val BLOCK_CLOSE: GameEvent = register("block_close")
    @JvmField
    val BLOCK_DEACTIVATE: GameEvent = register("block_deactivate")
    @JvmField
    val BLOCK_DESTROY: GameEvent = register("block_destroy")
    @JvmField
    val BLOCK_DETACH: GameEvent = register("block_detach")
    @JvmField
    val BLOCK_OPEN: GameEvent = register("block_open")
    @JvmField
    val BLOCK_PLACE: GameEvent = register("block_place")
    @JvmField
    val CONTAINER_CLOSE: GameEvent = register("container_close")
    @JvmField
    val CONTAINER_OPEN: GameEvent = register("container_open")
    @JvmField
    val DISPENSE_FAIL: GameEvent = register("dispense_fail")
    @JvmField
    val DRINK: GameEvent = register("drink")
    @JvmField
    val EAT: GameEvent = register("eat")
    @JvmField
    val ELYTRA_GLIDE: GameEvent = register("elytra_glide")
    @JvmField
    val ENTITY_DAMAGE: GameEvent = register("entity_damage")
    @JvmField
    val ENTITY_DIE: GameEvent = register("entity_die")
    @JvmField
    val ENTITY_INTERACT: GameEvent = register("entity_interact")
    @JvmField
    val ENTITY_PLACE: GameEvent = register("entity_place")
    @JvmField
    val ENTITY_ROAR: GameEvent = register("entity_roar")
    @JvmField
    val ENTITY_SHAKE: GameEvent = register("entity_shake")
    @JvmField
    val EQUIP: GameEvent = register("equip")
    @JvmField
    val EXPLODE: GameEvent = register("explode")
    @JvmField
    val FLAP: GameEvent = register("flap")
    @JvmField
    val FLUID_PICKUP: GameEvent = register("fluid_pickup")
    @JvmField
    val FLUID_PLACE: GameEvent = register("fluid_place")
    @JvmField
    val HIT_GROUND: GameEvent = register("hit_ground")
    @JvmField
    val INSTRUMENT_PLAY: GameEvent = register("instrument_play")
    @JvmField
    val ITEM_INTERACT_FINISH: GameEvent = register("item_interact_finish")
    @JvmField
    val ITEM_INTERACT_START: GameEvent = register("item_interact_start")
    @JvmField
    val LIGHTNING_STRIKE: GameEvent = register("lightning_strike")
    @JvmField
    val NOTE_BLOCK_PLAY: GameEvent = register("note_block_play")
    @JvmField
    val PISTON_CONTRACT: GameEvent = register("piston_contract")
    @JvmField
    val PISTON_EXTEND: GameEvent = register("piston_extend")
    @JvmField
    val PRIME_FUSE: GameEvent = register("prime_fuse")
    @JvmField
    val PROJECTILE_LAND: GameEvent = register("projectile_land")
    @JvmField
    val PROJECTILE_SHOOT: GameEvent = register("projectile_shoot")
    @JvmField
    val SCULK_SENSOR_TENDRILS_CLICKING: GameEvent = register("sculk_sensor_tendrils_clicking")
    @JvmField
    val SHEAR: GameEvent = register("shear")
    @JvmField
    val SHRIEK: GameEvent = register("shriek")
    @JvmField
    val SPLASH: GameEvent = register("splash")
    @JvmField
    val STEP: GameEvent = register("step")
    @JvmField
    val SWIM: GameEvent = register("swim")
    @JvmField
    val TELEPORT: GameEvent = register("teleport")

    @JvmStatic
    private fun register(name: String): GameEvent {
        val key = Key.key(name)
        return KryptonRegistries.register(KryptonRegistries.GAME_EVENT, key, GameEvent(key))
    }
}

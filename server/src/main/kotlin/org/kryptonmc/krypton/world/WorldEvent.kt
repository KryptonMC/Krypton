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
package org.kryptonmc.krypton.world

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * These are specific world events that will be sent by the server to easily
 * signal to clients when they occur. The seemingly arbitrary IDs are from
 * [here](https://wiki.vg/Protocol#Effect).
 */
enum class WorldEvent(val id: Int) {

    DISPENSER_DISPENSES(1000),
    DISPENSER_FAIL(1001),
    DISPENSER_PROJECTILE_LAUNCH(1002),
    ENDER_EYE_LAUNCH(1003),
    FIREWORK_SHOOT(1004),
    OPEN_IRON_DOOR(1005),
    OPEN_WOODEN_DOOR(1006),
    OPEN_WOODEN_TRAP_DOOR(1007),
    OPEN_FENCE_GATE(1008),
    EXTINGUISH_FIRE(1009),
    PLAY_RECORDING(1010),
    CLOSE_IRON_DOOR(1011),
    CLOSE_WOODEN_DOOR(1012),
    CLOSE_WOODEN_TRAP_DOOR(1013),
    CLOSE_FENCE_GATE(1014),
    GHAST_WARNING(1015),
    GHAST_FIREBALL(1016),
    DRAGON_FIREBALL(1017),
    BLAZE_FIREBALL(1018),
    ZOMBIE_WOODEN_DOOR(1019),
    ZOMBIE_IRON_DOOR(1020),
    ZOMBIE_DOOR_CRASH(1021),
    WITHER_BLOCK_BREAK(1022),
    WITHER_BOSS_SPAWN(1023),
    WITHER_BOSS_SHOOT(1024),
    BAT_LIFTOFF(1025),
    ZOMBIE_INFECTED(1026),
    ZOMBIE_CONVERTED(1027),
    DRAGON_DEATH(1028),
    ANVIL_BROKEN(1029),
    ANVIL_USED(1030),
    ANVIL_LAND(1031),
    PORTAL_TRAVEL(1032),
    CHORUS_GROW(1033),
    CHORUS_DEATH(1034),
    BREWING_STAND_BREW(1035),
    CLOSE_IRON_TRAP_DOOR(1036),
    OPEN_IRON_TRAP_DOOR(1037),
    END_PORTAL_SPAWN(1038),
    PHANTOM_BITE(1039),
    ZOMBIE_TO_DROWNED(1040),
    HUSK_TO_ZOMBIE(1041),
    GRINDSTONE_USED(1042),
    PAGE_TURN(1043),
    SMITHING_TABLE_USED(1044),
    POINTED_DRIPSTONE_LAND(1045),
    DRIP_LAVA_INTO_CAULDRON(1046),
    DRIP_WATER_INTO_CAULDRON(1047),
    SKELETON_TO_STRAY(1048),
    COMPOSTER_FILL(1500),
    LAVA_FIZZ(1501),
    REDSTONE_TORCH_BURNOUT(1502),
    END_PORTAL_FRAME_FILL(1503),
    DRIPSTONE_DRIP(1504),
    PLANT_GROWTH_BOTH(1505),
    SHOOT_SMOKE(2000),
    DESTROY_BLOCK(2001),
    POTION_SPLASH(2002),
    EYE_OF_ENDER_DEATH(2003),
    MOB_BLOCK_SPAWN(2004),
    PLANT_GROWTH(2005),
    DRAGON_FIREBALL_SPLASH(2006),
    INSTANT_POTION_SPLASH(2007),
    DRAGON_BLOCK_BREAK(2008),
    WATER_EVAPORATING(2009),
    END_GATEWAY_SPAWN(3000),
    DRAGON_SUMMON_ROAR(3001),
    ELECTRIC_SPARK(3002),
    WAX_ON(3003),
    WAX_OFF(3004),
    SCRAPE(3005);

    companion object {

        private val VALUES = WorldEvent.values()
        private val BY_ID = Int2ObjectOpenHashMap<WorldEvent>(VALUES.size).apply { VALUES.forEach { put(it.id, it) } }

        @JvmStatic
        fun fromId(id: Int): WorldEvent? = BY_ID.get(id)
    }
}

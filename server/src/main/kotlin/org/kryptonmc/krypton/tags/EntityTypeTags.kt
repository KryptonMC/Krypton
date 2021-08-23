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
package org.kryptonmc.krypton.tags

import net.kyori.adventure.key.Key

object EntityTypeTags {

    @JvmField val SKELETONS = get("skeletons")
    @JvmField val RAIDERS = get("raiders")
    @JvmField val BEEHIVE_INHABITORS = get("beehive_inhabitors")
    @JvmField val ARROWS = get("arrows")
    @JvmField val IMPACT_PROJECTILES = get("impact_projectiles")
    @JvmField val POWDER_SNOW_WALKABLE_MOBS = get("powder_snow_walkable_mobs")
    @JvmField val AXOLOTL_ALWAYS_HOSTILES = get("axolotl_always_hostiles")
    @JvmField val AXOLOTL_HUNT_TARGETS = get("axolotl_hunt_targets")
    @JvmField val FREEZE_IMMUNE_ENTITY_TYPES = get("freeze_immune_entity_types")
    @JvmField val FREEZE_HURTS_EXTRA_TYPES = get("freeze_hurts_extra_types")

    @JvmStatic
    operator fun get(key: Key) = TagManager[TagTypes.ENTITY_TYPES, key.asString()]

    @JvmStatic
    private fun get(name: String) = TagManager[TagTypes.ENTITY_TYPES, "minecraft:$name"]!!
}

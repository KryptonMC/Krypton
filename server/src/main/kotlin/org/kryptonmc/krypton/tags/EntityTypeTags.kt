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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryKeys

object EntityTypeTags {

    val SKELETONS = get("skeletons")
    val RAIDERS = get("raiders")
    val BEEHIVE_INHABITORS = get("beehive_inhabitors")
    val ARROWS = get("arrows")
    val IMPACT_PROJECTILES = get("impact_projectiles")
    val POWDER_SNOW_WALKABLE_MOBS = get("powder_snow_walkable_mobs")
    val AXOLOTL_ALWAYS_HOSTILES = get("axolotl_always_hostiles")
    val AXOLOTL_HUNT_TARGETS = get("axolotl_hunt_targets")
    val FREEZE_IMMUNE_ENTITY_TYPES = get("freeze_immune_entity_types")
    val FREEZE_HURTS_EXTRA_TYPES = get("freeze_hurts_extra_types")

    private fun get(name: String) = KryptonTagManager.load(Key.key(name), RegistryKeys.ENTITY_TYPE.location, "entity_types", Registries.ENTITY_TYPE)
}

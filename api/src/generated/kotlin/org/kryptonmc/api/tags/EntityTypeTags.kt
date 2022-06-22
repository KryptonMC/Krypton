/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Tag::class)
public object EntityTypeTags {

    // @formatter:off
    @JvmField
    public val SKELETONS: Tag<EntityType<*>> = get("skeletons")
    @JvmField
    public val RAIDERS: Tag<EntityType<*>> = get("raiders")
    @JvmField
    public val BEEHIVE_INHABITORS: Tag<EntityType<*>> = get("beehive_inhabitors")
    @JvmField
    public val ARROWS: Tag<EntityType<*>> = get("arrows")
    @JvmField
    public val IMPACT_PROJECTILES: Tag<EntityType<*>> = get("impact_projectiles")
    @JvmField
    public val POWDER_SNOW_WALKABLE_MOBS: Tag<EntityType<*>> = get("powder_snow_walkable_mobs")
    @JvmField
    public val AXOLOTL_ALWAYS_HOSTILES: Tag<EntityType<*>> = get("axolotl_always_hostiles")
    @JvmField
    public val AXOLOTL_HUNT_TARGETS: Tag<EntityType<*>> = get("axolotl_hunt_targets")
    @JvmField
    public val FREEZE_IMMUNE_ENTITY_TYPES: Tag<EntityType<*>> = get("freeze_immune_entity_types")
    @JvmField
    public val FREEZE_HURTS_EXTRA_TYPES: Tag<EntityType<*>> = get("freeze_hurts_extra_types")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Tag<EntityType<*>> = Krypton.tagManager[TagTypes.ENTITY_TYPES, "minecraft:$key"]!!
}

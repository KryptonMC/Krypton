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
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.resource.ResourceKeys

/**
 * This file is auto-generated. Do not edit this manually!
 */
public object EntityTypeTags {

    // @formatter:off
    @JvmField
    public val SKELETONS: TagKey<EntityType<*>> = get("skeletons")
    @JvmField
    public val RAIDERS: TagKey<EntityType<*>> = get("raiders")
    @JvmField
    public val BEEHIVE_INHABITORS: TagKey<EntityType<*>> = get("beehive_inhabitors")
    @JvmField
    public val ARROWS: TagKey<EntityType<*>> = get("arrows")
    @JvmField
    public val IMPACT_PROJECTILES: TagKey<EntityType<*>> = get("impact_projectiles")
    @JvmField
    public val POWDER_SNOW_WALKABLE_MOBS: TagKey<EntityType<*>> = get("powder_snow_walkable_mobs")
    @JvmField
    public val AXOLOTL_ALWAYS_HOSTILES: TagKey<EntityType<*>> = get("axolotl_always_hostiles")
    @JvmField
    public val AXOLOTL_HUNT_TARGETS: TagKey<EntityType<*>> = get("axolotl_hunt_targets")
    @JvmField
    public val FREEZE_IMMUNE_ENTITY_TYPES: TagKey<EntityType<*>> = get("freeze_immune_entity_types")
    @JvmField
    public val FREEZE_HURTS_EXTRA_TYPES: TagKey<EntityType<*>> = get("freeze_hurts_extra_types")
    @JvmField
    public val FROG_FOOD: TagKey<EntityType<*>> = get("frog_food")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): TagKey<EntityType<*>> = TagKey.of(ResourceKeys.ENTITY_TYPE, Key.key(key))
}

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
package org.kryptonmc.api.entity.attribute

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(AttributeType::class)
public object AttributeTypes {

    // @formatter:off
    @JvmField
    public val MAX_HEALTH: RegistryReference<AttributeType> = of("generic.max_health")
    @JvmField
    public val FOLLOW_RANGE: RegistryReference<AttributeType> = of("generic.follow_range")
    @JvmField
    public val KNOCKBACK_RESISTANCE: RegistryReference<AttributeType> = of("generic.knockback_resistance")
    @JvmField
    public val MOVEMENT_SPEED: RegistryReference<AttributeType> = of("generic.movement_speed")
    @JvmField
    public val FLYING_SPEED: RegistryReference<AttributeType> = of("generic.flying_speed")
    @JvmField
    public val ATTACK_DAMAGE: RegistryReference<AttributeType> = of("generic.attack_damage")
    @JvmField
    public val ATTACK_KNOCKBACK: RegistryReference<AttributeType> = of("generic.attack_knockback")
    @JvmField
    public val ATTACK_SPEED: RegistryReference<AttributeType> = of("generic.attack_speed")
    @JvmField
    public val ARMOR: RegistryReference<AttributeType> = of("generic.armor")
    @JvmField
    public val ARMOR_TOUGHNESS: RegistryReference<AttributeType> = of("generic.armor_toughness")
    @JvmField
    public val LUCK: RegistryReference<AttributeType> = of("generic.luck")
    @JvmField
    public val SPAWN_REINFORCEMENTS_CHANCE: RegistryReference<AttributeType> = of("zombie.spawn_reinforcements")
    @JvmField
    public val JUMP_STRENGTH: RegistryReference<AttributeType> = of("horse.jump_strength")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): RegistryReference<AttributeType> = RegistryReference.of(Registries.ATTRIBUTE, Key.key(name))
}

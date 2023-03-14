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
package org.kryptonmc.api.world.damage.type

import net.kyori.adventure.key.Keyed
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A type of damage to something.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(DamageTypes::class)
@ImmutableType
public interface DamageType : Keyed, Translatable {

    /**
     * If this damage type will damage its target's helmet.
     */
    @get:JvmName("damagesHelmet")
    public val damagesHelmet: Boolean

    /**
     * If this damage type bypasses protection from the target's armour.
     */
    @get:JvmName("bypassesArmor")
    public val bypassesArmor: Boolean

    /**
     * If this damage type bypasses invulnerability protection, such as a
     * player being in creative.
     */
    @get:JvmName("bypassesInvulnerability")
    public val bypassesInvulnerability: Boolean

    /**
     * If this damage type bypasses any protection offered by magic, such as
     * potions.
     */
    @get:JvmName("bypassesMagic")
    public val bypassesMagic: Boolean

    /**
     * The amount of exhaustion that this damage type will inflict upon a
     * target.
     */
    @get:JvmName("exhaustion")
    public val exhaustion: Float

    /**
     * If this damage type causes fire damage.
     */
    public val isFire: Boolean

    /**
     * If this damage type causes projectile damage.
     */
    public val isProjectile: Boolean

    /**
     * If this damage type's effects are increased as the difficulty increases.
     */
    @get:JvmName("scalesWithDifficulty")
    public val scalesWithDifficulty: Boolean

    /**
     * If this damage type causes magic damage.
     */
    public val isMagic: Boolean

    /**
     * If this damage type causes explosion damage.
     */
    public val isExplosion: Boolean

    /**
     * If this damage type causes fall damage.
     */
    public val isFall: Boolean

    /**
     * If this damage type causes thorns damage.
     */
    public val isThorns: Boolean

    /**
     * If the target this damage type is applied to will be aggravated by the
     * damage caused by applying this damage type.
     */
    @get:JvmName("aggravatesTarget")
    public val aggravatesTarget: Boolean
}

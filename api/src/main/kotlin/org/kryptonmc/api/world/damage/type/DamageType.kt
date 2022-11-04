/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.damage.type

import net.kyori.adventure.key.Keyed
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.api.util.CataloguedBy
import javax.annotation.concurrent.Immutable

/**
 * A type of damage to something.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(DamageTypes::class)
@Immutable
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

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

/**
 * An entity with a simple artificial intelligence that can drop items.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Mob : LivingEntity, Equipable {

    /**
     * If this mob is persistent (will have its data saved on removal).
     */
    public var isPersistent: Boolean

    /**
     * If this mob can pick up loot.
     *
     * For example, if this mob can wear armour/use weapons it picks up.
     */
    @get:JvmName("canPickUpLoot")
    public var canPickUpLoot: Boolean

    /**
     * If this mob has artificial intelligence.
     */
    @get:JvmName("hasAI")
    public var hasAI: Boolean

    /**
     * If this mob is hostile.
     */
    public var isAggressive: Boolean

    /**
     * The main hand of this mob.
     */
    public var mainHand: MainHand
}

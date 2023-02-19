/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.monster

/**
 * A creeper.
 */
public interface Creeper : Monster {

    /**
     * The radius of the explosion this creeper will produce when it explodes.
     *
     * In vanilla Minecraft, this will default to 3 for regular creepers, and
     * 6 for charged creepers, however these values may differ depending on the
     * implementation.
     */
    public var explosionRadius: Int

    /**
     * If this creeper is charged.
     *
     * In vanilla Minecraft, a creeper may be charged if it is struck by
     * lightning during a thunder storm.
     */
    public var isCharged: Boolean

    /**
     * If this creeper has been ignited, meaning it will explode when the
     * [fuse] reaches 0.
     */
    public val isIgnited: Boolean

    /**
     * The amount of time, in ticks, that it takes for the creeper to explode
     * after it is ignited.
     */
    public var fuse: Int

    /**
     * The current amount of time, in ticks, until the creeper will explode.
     *
     * When a creeper is [ignited][ignite], this value will be set to
     * the [fuse] time, and will decrease by 1 every tick until it reaches 0,
     * at which point the creeper will explode.
     *
     * If this creeper is not [ignited][isIgnited], this value will always
     * be -1.
     */
    public var currentFuse: Int

    /**
     * Ignites this creeper, which will cause it to explode after
     * the [fuse] time, in ticks, passes.
     */
    public fun ignite()

    /**
     * Explodes this creeper immediately, without igniting or waiting for the
     * countdown.
     */
    public fun explode()
}

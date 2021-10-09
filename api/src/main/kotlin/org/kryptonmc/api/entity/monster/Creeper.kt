/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.monster

/**
 * A creeper.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Creeper : Monster {

    /**
     * The fuse timer (time until the creeper will explode).
     *
     * Will be 0 if this creeper is not [ignited][isIgnited].
     */
    @get:JvmName("fuse")
    public var fuse: Short

    /**
     * The radius of the explosion this creeper will produce when it explodes.
     *
     * In vanilla Minecraft, this will default to 3 for regular creepers, and
     * 6 for charged creepers, however these values may differ depending on the
     * implementation.
     */
    @get:JvmName("explosionRadius")
    public val explosionRadius: Int

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
    public var isIgnited: Boolean
}

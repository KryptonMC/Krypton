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
public interface Creeper : Monster {

    /**
     * The fuse timer (time until the creeper will explode).
     *
     * Will be 0 if this creeper is not [ignited][isIgnited].
     */
    public var fuse: Short

    /**
     * The radius of the explosion this creeper will produce when it
     * explodes.
     *
     * Defaults to 3 for regular creepers and 6 for charged creepers.
     */
    public val explosionRadius: Int

    /**
     * If this creeper is charged (has been struck by lightning).
     */
    public var isCharged: Boolean

    /**
     * If this creeper has been ignited.
     */
    public var isIgnited: Boolean
}

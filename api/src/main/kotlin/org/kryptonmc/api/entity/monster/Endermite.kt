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
 * A monster that sometimes spawns when an ender pearl lands on a block.
 */
public interface Endermite : Monster {

    /**
     * The lifetime, in ticks, of this endermite.
     *
     * This is the total amount of time until it is removed, not how long left
     * it has until it is removed. For that, see [remainingLife].
     *
     * If this endermite is made [persistent][isPersistent], it will never
     * despawn, and this lifetime will be ignored.
     */
    public var life: Int

    /**
     * The remaining lifetime, in ticks, of this endermite.
     *
     * This is the amount of time remaining until this endermite is removed.
     */
    public val remainingLife: Int
}

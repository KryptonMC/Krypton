/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.user

import org.kryptonmc.api.entity.player.Player
import java.util.UUID

/**
 * A user. This is a player that can be offline, and mostly exists for that
 * purpose.
 */
public interface User : BaseUser {

    /**
     * The name of this user.
     */
    public val name: String

    /**
     * The UUID of this user.
     */
    public val uuid: UUID

    /**
     * Gets the player associated with this user, or null if they are not
     * currently available, usually meaning they are offline.
     *
     * @return the associated player
     */
    public fun asPlayer(): Player?
}

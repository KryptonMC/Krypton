/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.auth

import java.util.UUID

/**
 * Represents a player's authenticated Mojang game profile.
 */
interface GameProfile {

    /**
     * The name of the profile.
     */
    val name: String

    /**
     * The UUID of the profile.
     */
    val uuid: UUID

    /**
     * The list of properties for this profile.
     */
    val properties: List<ProfileProperty>
}

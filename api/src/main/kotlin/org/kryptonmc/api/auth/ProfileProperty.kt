/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.auth

/**
 * A property of a [GameProfile].
 */
interface ProfileProperty {

    /**
     * The name of the property.
     */
    val name: String

    /**
     * The value of the property.
     */
    val value: String

    /**
     * The Yggdrasil signature for this property. May be null if this property isn't
     * signed.
     */
    val signature: String?
}

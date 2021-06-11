/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event

/**
 * Represents priorities of a listener.
 *
 * This is an object with const values as opposed to an enum
 * because BungeeCord.
 */
enum class ListenerPriority {

    MAXIMUM,
    VERY_HIGH,
    HIGH,
    MEDIUM,
    LOW,
    VERY_LOW,
    NONE
}

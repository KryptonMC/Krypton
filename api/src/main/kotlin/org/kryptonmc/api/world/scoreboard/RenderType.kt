/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.scoreboard

import org.kryptonmc.api.util.StringSerializable

/**
 * Controls how an [Objective] is rendered to the client.
 */
public enum class RenderType(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    /**
     * Display an integer value.
     */
    INTEGER("integer"),

    /**
     * Display a number of hearts corresponding to the value.
     */
    HEARTS("hearts")
}

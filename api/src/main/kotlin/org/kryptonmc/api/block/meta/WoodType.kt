/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

import org.kryptonmc.api.util.StringSerializable

/**
 * The available types of wood.
 */
public enum class WoodType(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    OAK("oak"),
    SPRUCE("spruce"),
    BIRCH("birch"),
    ACACIA("acacia"),
    JUNGLE("jungle"),
    DARK_OAK("dark_oak"),
    CRIMSON("crimson"),
    WARPED("warped")
}

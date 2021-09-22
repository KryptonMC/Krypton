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
 * The tilt of a big dripleaf.
 *
 * @param causesVibrations if this tilt causes vibrations
 */
public enum class Tilt(
    @get:JvmName("serialized") override val serialized: String,
    @get:JvmName("causesVibrations") public val causesVibrations: Boolean
) : StringSerializable {

    NONE("none", true),
    UNSTABLE("unstable", false),
    PARTIAL("partial", true),
    FULL("full", true)
}

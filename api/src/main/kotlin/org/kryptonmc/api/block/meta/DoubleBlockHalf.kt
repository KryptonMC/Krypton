/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

import org.kryptonmc.api.util.StringSerializable

/**
 * The half of a 2 block tall block.
 */
public enum class DoubleBlockHalf(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    UPPER("upper"),
    LOWER("lower")
}

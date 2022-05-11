/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

/**
 * Indicates the thickness of a dripstone block that forms the multi block
 * stalagmites and stalagtites.
 *
 * As stalagmites and stalagtites are spikes on the ceiling or the ground,
 * and they vary in size, it is important to be able to track which part of it
 * a specific block is. For example, a three block stalagmite may have a base,
 * the part connected to the ground, a middle, the section between the base
 * and the top, and the tip, the section at the top that ends the stalagmite.
 */
public enum class DripstoneThickness {

    TIP_MERGE,
    TIP,
    FRUSTUM,
    MIDDLE,
    BASE
}

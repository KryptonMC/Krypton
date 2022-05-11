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
 * Indicates the phase that a sculk sensor this property is applied to is
 * currently in.
 */
// TODO: Find out about sculk sensor phases
public enum class SculkSensorPhase {

    INACTIVE,
    ACTIVE,
    COOLDOWN
}

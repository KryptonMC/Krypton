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
 * Indicates the type of piston a piston block this property is applied to
 * represents.
 */
public enum class PistonType {

    /**
     * Normal pistons will push blocks on extension, and do nothing on
     * retraction.
     */
    NORMAL,

    /**
     * Sticky pistons will push blocks on extension, and pull blocks on
     * retraction, as blocks will stick to them.
     */
    STICKY
}

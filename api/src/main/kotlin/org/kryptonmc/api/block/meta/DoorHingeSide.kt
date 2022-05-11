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
 * Indicates the side of the door that the hinges will appear on, and the
 * direction the door will open in when it is opened.
 */
public enum class DoorHingeSide {

    /**
     * The hinges will appear on the left side of the door, and the door will
     * open to the left.
     */
    LEFT,

    /**
     * The hinges will appear on the right side of the door, and the door will
     * open to the right.
     */
    RIGHT
}

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
 * Indicates the mode that a block this property is applied to is operating
 * under.
 *
 * Comparators have three input sides, and two inputs. The back is the main
 * input, which is the side with two torches. The left and right are combined
 * in to the secondary input, by taking the maximum of them.
 *
 * Comparators have two different modes that change their behaviour, which are
 * described on their respective enum constants.
 */
public enum class ComparatorMode {

    /**
     * In this mode, the comparator compares the signal strength of the main
     * input to the signal strength of the secondary input, and outputs the
     * greatest signal strength.
     */
    COMPARE,

    /**
     * In this mode, the comparator outputs the result of subtracting the
     * signal strength of the secondary input from the signal strength of the
     * main input.
     */
    SUBTRACT
}

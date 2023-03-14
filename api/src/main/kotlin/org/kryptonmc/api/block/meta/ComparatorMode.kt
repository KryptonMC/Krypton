/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

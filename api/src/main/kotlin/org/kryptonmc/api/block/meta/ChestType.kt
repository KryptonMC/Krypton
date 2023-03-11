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
 * Indicates the type of chest a block this property is applied to represents.
 *
 * This is used to help the client properly attach chest models, and combine
 * adjacent chest blocks where necessary, for example, with the double chest,
 * where adjacent chests need to be attached across blocks.
 */
public enum class ChestType(private val oppositeId: Int) {

    /**
     * A single chest that is not connected to any other chest.
     */
    SINGLE(0),

    /**
     * The left half of a double chest.
     */
    LEFT(2),

    /**
     * The right half of a double chest.
     */
    RIGHT(1);

    /**
     * Gets the chest type opposite to this chest type.
     *
     * If this chest is the left half of a double chest, the opposite will be
     * the right half, and if it is the right half, the opposite will be the
     * left half. If this chest is a single chest, it has no opposite type,
     * and so the opposite will just be single.
     *
     * @return the opposite chest type
     */
    public fun opposite(): ChestType = BY_ID[oppositeId]

    public companion object {

        private val BY_ID = values()
    }
}

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
 * Indicates how a bell block that this may be applied to attaches to the
 * block it was placed on.
 */
public enum class BellAttachment {

    /**
     * The bell is attached to the top face of the block below it.
     */
    FLOOR,

    /**
     * The bell is attached to the bottom face of the block above it.
     */
    CEILING,

    /**
     * The bell is attached to the north, south, east, or west face of a
     * single block to the north, south, east, or west of it.
     */
    SINGLE_WALL,

    /**
     * The bell is attached to the north/south or east/west face of two blocks
     * on opposing sides of the bell, either the north/south or east/west sides.
     */
    DOUBLE_WALL
}

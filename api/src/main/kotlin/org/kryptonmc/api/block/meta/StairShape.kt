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
 * Indicates the shape that a stair block may appear in to ensure it properly
 * connects to its neighbouring stair blocks.
 */
public enum class StairShape {

    /**
     * The stairs do not bend at all. They appear in a straight line, where
     * the end faces are on opposing sides.
     */
    STRAIGHT,

    /**
     * The stairs turn inwards to the left. The missing section of the stairs
     * will only occupy 1/8 of the full block, rather than 1/4 (2/8), like the
     * straight stairs.
     *
     * The turn will be in the opposite direction to the [INNER_RIGHT] turn.
     */
    INNER_LEFT,

    /**
     * The stairs turn inwards to the right. The missing section of the stairs
     * will only occupy 1/8 of the full block, rather than 1/4 (2/8), like the
     * straight stairs.
     *
     * The turn will be in the opposite direction to the [INNER_LEFT] turn.
     */
    INNER_RIGHT,

    /**
     * The stairs will turn outwards to the left. The missing section of the
     * stairs will occupy 3/8 of the full block, rather than 1/4 (2/8), like
     * the straight stairs.
     *
     * The turn will be in the opposite direction to the [OUTER_RIGHT] turn.
     */
    OUTER_LEFT,

    /**
     * The stairs will turn outwards to the right. The missing section of the
     * stairs will occupy 3/8 of the full block, rather than 1/4 (2/8), like
     * the straight stairs.
     *
     * The turn will be in the opposite direction to the [OUTER_LEFT] turn.
     */
    OUTER_RIGHT
}

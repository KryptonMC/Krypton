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
 * Indicates the type of slab that a block this property is applied to
 * represents.
 *
 * Slabs are different from a lot of other blocks, in that two of them can be
 * placed in the same block space. However, for uniformity, this is all an
 * illusion, and this property is used to differentiate "half slabs", those
 * that occupy half of a block from "full slabs", those that occupy a full
 * block, when two slabs are placed in the same block.
 */
public enum class SlabType {

    /**
     * The slab occupies the top half of a block.
     */
    TOP,

    /**
     * The slab occupies the bottom half of a block.
     */
    BOTTOM,

    /**
     * The slab occupies both halves of a block. This happens when two slabs
     * are placed in the same block.
     */
    DOUBLE
}

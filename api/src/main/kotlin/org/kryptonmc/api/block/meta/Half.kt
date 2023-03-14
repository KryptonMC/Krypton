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
 * Indicates which variant of a block that can attach to either the block
 * above it or the block below it, such as stairs or trapdoors, a block this
 * property is applied to represents.
 */
public enum class Half {

    /**
     * The block is attached to the block above it.
     */
    TOP,

    /**
     * The block is attached to the block below it.
     */
    BOTTOM
}

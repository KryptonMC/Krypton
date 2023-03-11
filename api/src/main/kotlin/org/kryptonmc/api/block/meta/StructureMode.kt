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

import net.kyori.adventure.translation.Translatable

/**
 * Indicates the mode that a structure block this property is applied to is in.
 *
 * The mode of a structure block influences its behaviour. For example, in load
 * mode, the structure block will load a saved structure. In save mode, it will
 * save a captured structure.
 */
public enum class StructureMode : Translatable {

    /**
     * In this mode, a structure block can load a saved structure in to the
     * world.
     */
    LOAD,

    /**
     * In this mode, a structure block can save a captured structure for use
     * later.
     */
    SAVE,

    /**
     * In this mode, a structure block will act as a marker for the opposing
     * corner of a structure block in save mode.
     *
     * This is used to make capturing structures easier, and ensure that no
     * unwanted part of a structure is included in the save.
     */
    CORNER,

    /**
     * In this mode, a structure block will mark the location of a function to
     * run, specified by its metadata input. This only works for specific
     * structures, and only during natural generation.
     */
    DATA;

    override fun translationKey(): String = "structure_block.mode_info.${name.lowercase()}"
}

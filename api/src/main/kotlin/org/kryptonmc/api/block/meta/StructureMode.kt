/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

import net.kyori.adventure.text.Component

/**
 * Indicates the mode that a structure block this property is applied to is in.
 *
 * The mode of a structure block influences its behaviour. For example, in load
 * mode, the structure block will load a saved structure. In save mode, it will
 * save a captured structure.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public enum class StructureMode {

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

    /**
     * The display name of this structure mode, as shown in the client's
     * structure block configuration menu.
     */
    @get:JvmName("displayName")
    public val displayName: Component = Component.translatable("structure_block.mode_info.${name.lowercase()}")
}

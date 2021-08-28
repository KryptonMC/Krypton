/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.util.StringSerializable

/**
 * The mode of a structure block.
 */
public enum class StructureMode(override val serialized: String) : StringSerializable {

    LOAD("load"),
    SAVE("save"),
    CORNER("corner"),
    DATA("data");

    /**
     * The client-side translation for this structure mode.
     */
    public val translation: TranslatableComponent = Component.translatable("structure_block.mode_info.$serialized")
}

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
import org.kryptonmc.api.util.TranslationHolder

/**
 * The mode of a structure block.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public enum class StructureMode(@get:JvmName("serialized") override val serialized: String) : StringSerializable, TranslationHolder {

    LOAD("load"),
    SAVE("save"),
    CORNER("corner"),
    DATA("data");

    /**
     * The client-side translation for this structure mode.
     */
    @get:JvmName("translation")
    public override val translation: TranslatableComponent = Component.translatable("structure_block.mode_info.$serialized")
}

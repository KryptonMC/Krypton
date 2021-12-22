/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import net.kyori.adventure.text.Component
import org.kryptonmc.api.util.TranslationHolder

/**
 * A block entity that has an associated custom name.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface NameableBlockEntity : BlockEntity, TranslationHolder {

    /**
     * The display name of this block entity.
     */
    @get:JvmName("displayName")
    public var displayName: Component
}

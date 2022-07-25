/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import net.kyori.adventure.text.Component
import org.kryptonmc.api.item.data.DyeColor

/**
 * A sign.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Sign : BlockEntity {

    /**
     * The lines of text on this sign.
     */
    @get:JvmName("lines")
    public val lines: Collection<Component>

    /**
     * Whether this sign is editable.
     */
    public var isEditable: Boolean

    /**
     * Whether this sign has text that is glowing.
     */
    public var isTextGlowing: Boolean

    /**
     * The colour that this sign is dyed.
     */
    public var color: DyeColor

    /**
     * Gets the line of text on this sign at the given [index].
     *
     * @param index the index
     * @return the line
     */
    public fun line(index: Int): Component

    /**
     * Sets the line of text on this sign at the given [index] to the given
     * [line].
     *
     * @param index the index
     * @param line the line
     */
    public fun setLine(index: Int, line: Component)
}

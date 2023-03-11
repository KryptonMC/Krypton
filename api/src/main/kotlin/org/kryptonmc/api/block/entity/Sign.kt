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
package org.kryptonmc.api.block.entity

import net.kyori.adventure.text.Component
import org.kryptonmc.api.item.data.DyeColor

/**
 * A sign.
 */
public interface Sign : BlockEntity {

    /**
     * The lines of text on this sign.
     */
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
    public fun getLine(index: Int): Component

    /**
     * Sets the line of text on this sign at the given [index] to the given
     * [line].
     *
     * @param index the index
     * @param line the line
     */
    public fun setLine(index: Int, line: Component)
}

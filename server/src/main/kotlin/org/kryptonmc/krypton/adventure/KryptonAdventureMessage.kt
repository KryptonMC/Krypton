/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.adventure

import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.AdventureMessage
import org.kryptonmc.api.adventure.toPlainText

@JvmRecord
data class KryptonAdventureMessage(override val wrapped: Component) : AdventureMessage {

    override fun getString(): String = wrapped.toPlainText()

    override fun asComponent(): Component = wrapped

    object Factory : AdventureMessage.Factory {

        override fun of(wrapped: Component): AdventureMessage = KryptonAdventureMessage(wrapped)
    }
}

/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.locale

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component

interface Message {

    fun interface Args0 {

        fun build(): Component

        fun send(sender: Audience) {
            sender.sendMessage(build())
        }
    }

    fun interface Args1<A> {

        fun build(a: A): Component

        fun send(sender: Audience, a: A) {
            sender.sendMessage(build(a))
        }
    }

    fun interface Args2<A, B> {

        fun build(a: A, b: B): Component

        fun send(sender: Audience, a: A, b: B) {
            sender.sendMessage(build(a, b))
        }
    }

    fun interface Args3<A, B, C> {

        fun build(a: A, b: B, c: C): Component

        fun send(sender: Audience, a: A, b: B, c: C) {
            sender.sendMessage(build(a, b, c))
        }
    }
}

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
package org.kryptonmc.codegen

fun String.camelCase(): String {
    val builder = StringBuilder(this)
    var i = 0
    while (i < builder.length) {
        if (builder[i] == '_') {
            builder.deleteCharAt(i)
            builder.replace(i, i + 1, builder[i].uppercase())
        }
        i++
    }
    builder.setCharAt(0, builder[0].uppercaseChar())
    return builder.toString()
}

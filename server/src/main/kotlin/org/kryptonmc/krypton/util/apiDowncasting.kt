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
package org.kryptonmc.krypton.util

/**
 * Downcasts the given API type [A] to its implementation type [I].
 *
 * This is a common implementation shared between specific downcast functions
 * that are used to downcast a specific API type to its implementation
 * equivalent when implementation-specific information is required.
 */
inline fun <A, reified I : A> A.downcastApiType(name: String): I {
    check(this is I) { "Custom implementations of $name are not supported!" }
    return this
}

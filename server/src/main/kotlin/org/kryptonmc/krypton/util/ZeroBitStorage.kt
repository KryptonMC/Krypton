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
package org.kryptonmc.krypton.util

class ZeroBitStorage(override val size: Int) : BitStorage {

    override val bits = 0
    override val data = RAW

    override fun getAndSet(index: Int, value: Int): Int {
        require(index in 0 until size) { "Index must be between 0 and $size, was $index" }
        require(value == 0) { "Value must be 0, was $value" }
        return 0
    }

    override fun get(index: Int): Int {
        require(index in 0 until size) { "Index must be between 0 and $size, was $index" }
        return 0
    }

    override fun set(index: Int, value: Int) {
        require(index in 0 until size) { "Index must be between 0 and $size, was $index" }
        require(value == 0) { "Value must be 0, was $value" }
    }

    override fun forEach(consumer: StorageConsumer) {
        for (i in 0 until size) {
            consumer(i, 0)
        }
    }

    override fun unpack(output: IntArray) {
        output.fill(0)
    }

    companion object {

        private val RAW = LongArray(0)
    }
}

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
package org.kryptonmc.krypton.util.bits

class ZeroBitStorage(size: Int) : AbstractBitStorage(size) {

    override val bits: Int
        get() = 0
    override val data: LongArray
        get() = RAW

    override fun getAndSet(index: Int, value: Int): Int {
        checkIndex(index)
        checkValue(value)
        return 0
    }

    override fun get(index: Int): Int {
        checkIndex(index)
        return 0
    }

    override fun set(index: Int, value: Int) {
        checkIndex(index)
        checkValue(value)
    }

    private fun checkValue(value: Int) {
        require(value == 0) { "Value must be 0, was $value!" }
    }

    override fun forEach(consumer: StorageConsumer) {
        for (i in 0 until size) {
            consumer.accept(i, 0)
        }
    }

    override fun unpack(output: IntArray) {
        output.fill(0)
    }

    override fun copy(): BitStorage = this

    companion object {

        private val RAW = LongArray(0)
    }
}

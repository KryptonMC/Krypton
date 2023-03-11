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

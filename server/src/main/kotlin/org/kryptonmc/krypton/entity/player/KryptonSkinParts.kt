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
package org.kryptonmc.krypton.entity.player

import org.kryptonmc.api.entity.player.SkinParts

class KryptonSkinParts(private val raw: Int) : SkinParts {

    override fun hasCape(): Boolean = raw and FLAG_CAPE != 0

    override fun hasJacket(): Boolean = raw and FLAG_JACKET != 0

    override fun hasLeftSleeve(): Boolean = raw and FLAG_LEFT_SLEEVE != 0

    override fun hasRightSleeve(): Boolean = raw and FLAG_RIGHT_SLEEVE != 0

    override fun hasLeftPants(): Boolean = raw and FLAG_LEFT_PANTS != 0

    override fun hasRightPants(): Boolean = raw and FLAG_RIGHT_PANTS != 0

    override fun hasHat(): Boolean = raw and FLAG_HAT != 0

    override fun equals(other: Any?): Boolean = this === other || other is KryptonSkinParts && raw == other.raw

    override fun hashCode(): Int = raw.hashCode()

    override fun toString(): String = "SkinParts(raw=$raw)"

    companion object {

        private const val FLAG_CAPE = 0x01
        private const val FLAG_JACKET = 0x02
        private const val FLAG_LEFT_SLEEVE = 0x04
        private const val FLAG_RIGHT_SLEEVE = 0x08
        private const val FLAG_LEFT_PANTS = 0x10
        private const val FLAG_RIGHT_PANTS = 0x20
        private const val FLAG_HAT = 0x40

        @JvmField
        val ALL: SkinParts = KryptonSkinParts(127)
    }
}

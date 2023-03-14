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
package org.kryptonmc.krypton.state.property

import com.google.common.collect.ImmutableSet

class BooleanProperty(name: String) : KryptonProperty<Boolean>(name, Boolean::class.javaObjectType) {

    override val values: Collection<Boolean>
        get() = VALUES

    override fun idFor(value: Boolean): Int = if (value) 1 else 0

    override fun fromString(value: String): Boolean? = value.toBooleanStrictOrNull()

    override fun toString(value: Boolean): String = value.toString()

    @Suppress("MagicNumber") // This is a hash code function
    override fun generateHashCode(): Int = 31 * super.generateHashCode() + VALUES.hashCode()

    companion object {

        private val VALUES = ImmutableSet.of(true, false)
    }
}

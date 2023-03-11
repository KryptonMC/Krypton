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
@file:JvmSynthetic
package org.kryptonmc.api.state

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * Represents a property key.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Properties::class)
@ImmutableType
public interface Property<T : Comparable<T>> {

    /**
     * The name of the property key.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The type of this property key.
     */
    @get:JvmName("type")
    public val type: Class<T>

    /**
     * The set of values this property key allows.
     */
    @get:JvmName("values")
    public val values: @Unmodifiable Collection<T>

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun forBoolean(name: String): Property<Boolean>

        public fun forInt(name: String): Property<Int>

        public fun <E : Enum<E>> forEnum(name: String): Property<E>
    }
}

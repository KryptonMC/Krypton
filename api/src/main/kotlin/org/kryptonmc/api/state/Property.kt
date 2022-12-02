/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.state

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
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

    public companion object {

        @JvmSynthetic
        internal fun forBoolean(name: String): Property<Boolean> = Krypton.factory<Factory>().forBoolean(name)

        @JvmSynthetic
        internal fun forInt(name: String): Property<Int> = Krypton.factory<Factory>().forInt(name)

        @JvmSynthetic
        internal fun <E : Enum<E>> forEnum(name: String): Property<E> = Krypton.factory<Factory>().forEnum(name)
    }
}

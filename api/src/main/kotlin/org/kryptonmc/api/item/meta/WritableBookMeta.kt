/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Item metadata for a writable book (book and quill).
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface WritableBookMeta : BookMeta<WritableBookMeta.Builder, WritableBookMeta> {

    /**
     * A builder for building writable book metadata.
     */
    @MetaDsl
    public interface Builder : BookMeta.Builder<Builder, WritableBookMeta>

    public companion object {

        /**
         * Creates a new builder for building writable book metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(WritableBookMeta::class.java)
    }
}

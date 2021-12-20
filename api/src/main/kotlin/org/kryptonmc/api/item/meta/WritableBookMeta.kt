/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

/**
 * Item metadata for a writable book (book and quill).
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface WritableBookMeta : BookMeta<WritableBookMeta>, ItemMetaBuilder.Provider<WritableBookMeta.Builder> {

    /**
     * A builder for building writable book metadata.
     */
    public interface Builder : BookMeta.Builder<Builder, WritableBookMeta>

    public companion object {

        /**
         * Creates a new builder for building writable book metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun builder(): Builder = ItemMeta.FACTORY.builder(WritableBookMeta::class.java)
    }
}

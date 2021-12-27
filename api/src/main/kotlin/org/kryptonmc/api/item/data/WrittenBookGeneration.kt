/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.data

/**
 * A generation that a written book is in. This is used to describe how many
 * times the written book has been copied.
 */
public enum class WrittenBookGeneration {

    /**
     * An original copy. Books in this generation have not been copied from
     * other sources, and are from the source.
     */
    ORIGINAL,

    /**
     * The first copy of a book. Also known as "second-hand".
     */
    COPY_OF_ORIGINAL,

    /**
     * The second copy of a book. This is the last copy that can be made of a
     * book.
     */
    COPY_OF_COPY,

    /**
     * Unused in vanilla, and functions the same as [COPY_OF_COPY].
     */
    TATTERED;

    public companion object {

        private val VALUES = values()

        /**
         * Gets the written book generation with the given [id], or returns
         * null if there is no written book generation with the given [id].
         *
         * @param id the ID
         * @return the generation with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): WrittenBookGeneration? = VALUES.getOrNull(id)
    }
}

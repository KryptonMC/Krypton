/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.type

import org.kryptonmc.api.util.StringSerializable

/**
 * A gene that a panda may possess.
 *
 * @param isRecessive if this gene is a recessive allele
 */
public enum class PandaGene(
    @get:JvmName("serialized") override val serialized: String,
    public val isRecessive: Boolean
) : StringSerializable {

    NORMAL("normal", false),
    LAZY("lazy", false),
    WORRIED("worried", false),
    PLAYFUL("playful", false),
    BROWN("brown", true),
    WEAK("weak", true),
    AGGRESSIVE("aggressive", false);

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }

        /**
         * Gets the panda gene with the given [name], or returns null if there
         * is no panda gene with the given [name].
         *
         * @param name the name
         * @return the panda gene with the name, or null if not present
         */
        @JvmStatic
        public fun fromName(name: String): PandaGene? = BY_NAME[name]

        /**
         * Gets the panda gene with the given [id], or returns null if there
         * is no panda gene with the given [id].
         *
         * @param id the ID
         * @return the panda gene with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): PandaGene? = VALUES.getOrNull(id)
    }
}

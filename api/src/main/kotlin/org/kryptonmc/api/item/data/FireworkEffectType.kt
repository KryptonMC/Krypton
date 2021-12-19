package org.kryptonmc.api.item.data

/**
 * A type of firework effect.
 */
public enum class FireworkEffectType {

    SMALL_BALL,
    LARGE_BALL,
    STAR,
    CREEPER,
    BURST;

    public companion object {

        private val VALUES = values()

        /**
         * Gets the firework effect type with the given [id], or returns null
         * if there is no firework effect type with the given [id].
         *
         * @param id the ID
         * @return the firework effect type with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): FireworkEffectType? = VALUES.getOrNull(id)
    }
}

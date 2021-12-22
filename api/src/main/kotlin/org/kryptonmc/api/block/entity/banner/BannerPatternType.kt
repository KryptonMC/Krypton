package org.kryptonmc.api.block.entity.banner

import org.kryptonmc.api.util.StringSerializable

/**
 * A type of banner pattern.
 *
 * @param code the shortened code identifying the banner pattern, as specified
 * by https://minecraft.fandom.com/wiki/Banner#Block_data
 */
public enum class BannerPatternType(
    @get:JvmName("serialized") override val serialized: String,
    public val code: String
) : StringSerializable {

    BASE("base", "b"),
    TOP_LEFT_SQUARE("top_left_square", "tl"),
    TOP_RIGHT_SQUARE("top_right_square", "tr"),
    BOTTOM_LEFT_SQUARE("bottom_left_square", "bl"),
    BOTTOM_RIGHT_SQUARE("bottom_right_square", "br"),
    TOP_STRIPE("top_stripe", "ts"),
    BOTTOM_STRIPE("bottom_stripe", "bs"),
    LEFT_STRIPE("left_stripe", "ls"),
    RIGHT_STRIPE("right_stripe", "rs"),
    CENTER_STRIPE("center_stripe", "cs"),
    MIDDLE_STRIPE("middle_stripe", "ms"),
    DOWN_LEFT_STRIPE("down_left_stripe", "dls"),
    DOWN_RIGHT_STRIPE("down_right_stripe", "drs"),
    SMALL_STRIPES("small_stripes", "ss"),
    CROSS("cross", "cr"),
    STRAIGHT_CROSS("straight_cross", "sc"),
    TOP_TRIANGLE("top_triangle", "tt"),
    BOTTOM_TRIANGLE("bottom_triangle", "bt"),
    TOP_TRIANGLES("top_triangles", "tts"),
    BOTTOM_TRIANGLES("bottom_triangles", "bts"),
    LEFT_DIAGONAL("left_diagonal", "ld"),
    RIGHT_DIAGONAL("right_diagonal", "rd"),
    LEFT_REVERSE_DIAGONAL("left_reverse_diagonal", "lud"),
    RIGHT_REVERSE_DIAGONAL("right_reverse_diagonal", "rud"),
    MIDDLE_CIRCLE("middle_circle", "mc"),
    MIDDLE_RHOMBUS("middle_rhombus", "mr"),
    TOP_HALF_HORIZONTAL("top_half_horizontal", "hh"),
    BOTTOM_HALF_HORIZONTAL("bottom_half_horizontal", "hhb"),
    LEFT_HALF_VERTICAL("left_half_vertical", "vh"),
    RIGHT_HALF_VERTICAL("right_half_vertical", "vhr"),
    BORDER("border", "bo"),
    CURLY_BORDER("curly_border", "cbo"),
    GRADIENT("gradient", "gra"),
    REVERSE_GRADIENT("reverse_gradient", "gru"),
    BRICKS("bricks", "bri"),
    GLOBE("globe", "glb"),
    CREEPER("creeper", "cre"),
    SKULL("skull", "sku"),
    FLOWER("flower", "flo"),
    MOJANG("mojang", "moj"),
    PIGLIN("piglin", "pig");

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }
        private val BY_CODE = VALUES.associateBy { it.code }

        /**
         * Gets the banner pattern type with the given [name], or returns null
         * if there is no banner pattern type with the given [name].
         *
         * @param name the name
         * @return the type with the name, or null if not present
         */
        @JvmStatic
        public fun fromName(name: String): BannerPatternType? = BY_NAME[name]

        /**
         * Gets the banner pattern type with the given [code], or returns null
         * if there is no banner pattern type with the given [code].
         *
         * @param code the code
         * @return the type with the code, or null if not present
         */
        @JvmStatic
        public fun fromCode(code: String): BannerPatternType? = BY_CODE[code]
    }
}

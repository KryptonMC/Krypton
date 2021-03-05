package org.kryptonmc.krypton.lang;

import kotlinx.serialization.Serializable

// TODO: Look into removing this as Adventure has it's own boss bars
@Serializable
enum class LegacyColor(val char: Char) {

    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_CYAN('3'),
    DARK_RED('4'),
    PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    CYAN('b'),
    RED('c'),
    PINK('d'),
    YELLOW('e'),
    WHITE('f');

    fun toChatString() = "§$char"

    companion object {

        private val VALUES = values().associateBy { it.char }

        fun fromLegacy(legacy: String): LegacyColor {
            val code = legacy.toCharArray()[1]
            return VALUES.getValue(code)
        }
    }
}
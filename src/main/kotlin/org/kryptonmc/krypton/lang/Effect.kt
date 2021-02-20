package org.kryptonmc.krypton.lang

@Deprecated("We now use Komponent for this")
enum class Effect(private val char: Char) {

    OBFUSCATED('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINED('n'),
    ITALIC('o'),
    RESET('r');

    fun toChatString() = "§$char"
}
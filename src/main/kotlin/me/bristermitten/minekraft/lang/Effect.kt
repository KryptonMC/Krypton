package me.bristermitten.minekraft.lang


enum class Effect(private val char: Char) {
    OBFUSCATED('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINED('n'),
    ITALIC('o'),
    RESET('r');

    fun toChatString() = "${COLOR_CHAR}$char"
}

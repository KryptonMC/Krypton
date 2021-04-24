package org.kryptonmc.krypton.util

import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import java.text.MessageFormat
import java.util.Locale

/**
 * Register console locale strings. This is temporary, and will be replaced with a proper
 * locale system when Krypton has one.
 */
object TranslationRegister {

    private val registry = TranslationRegistry.create(Key.key("krypton", "console"))

    fun initialize() {
        registry.register("multiplayer.player.joined", Locale.ENGLISH, MessageFormat("{0} joined the game"))
        registry.register("multiplayer.player.left", Locale.ENGLISH, MessageFormat("{0} left the game"))
        registry.register("chat.type.text", Locale.ENGLISH, MessageFormat("<{0}> {1}"))
        GlobalTranslator.get().addSource(registry)
    }
}

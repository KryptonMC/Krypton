package org.kryptonmc.krypton.console

import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.translation.GlobalTranslator
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.util.logger
import java.util.Locale

/**
 * Represents a sender for the server console.
 */
class ConsoleSender(server: KryptonServer) : KryptonSender(server) {

    override val name = "CONSOLE"

    override fun identity() = Identity.nil()

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        val component = when (message) {
            is TranslatableComponent -> GlobalTranslator.render(message, Locale.ENGLISH)
            is TextComponent -> message
            else -> return
        }
        LOGGER.info(LegacyComponentSerializer.legacySection().serialize(component))
    }

    override fun hasPermission(permission: String) = true // we are literally god, we never fail permission checks

    companion object {

        private val LOGGER = logger("CONSOLE")
    }
}

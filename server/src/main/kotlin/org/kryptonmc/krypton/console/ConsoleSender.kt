package org.kryptonmc.krypton.console

import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.command.KryptonSender

class ConsoleSender(override val name: String) : KryptonSender() {

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        LOGGER.info(LegacyComponentSerializer.legacySection().serialize(message))
    }

    companion object {

        private val LOGGER = LogManager.getLogger("CONSOLE")
    }
}
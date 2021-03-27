package org.kryptonmc.krypton.console

import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.extension.logger

class ConsoleSender(server: KryptonServer) : KryptonSender(server) {

    override val name = "CONSOLE"

    override fun identity() = Identity.nil()

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        LOGGER.info(LegacyComponentSerializer.legacySection().serialize(message))
    }

    override fun hasPermission(permission: String) = true // we are literally god, we never fail permission checks

    companion object {

        private val LOGGER = logger("CONSOLE")
    }
}
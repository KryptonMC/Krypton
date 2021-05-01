package org.kryptonmc.krypton.command.commands

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.command.Command
import org.kryptonmc.krypton.api.command.Sender

class RestartCommand(private val server: KryptonServer) : Command("restart", "krypton.command.restart") {

    override fun execute(sender: Sender, args: List<String>) {
        sender.sendMessage(Component.text("Attempting to restart the server..."))
        server.stop(true)
    }
}

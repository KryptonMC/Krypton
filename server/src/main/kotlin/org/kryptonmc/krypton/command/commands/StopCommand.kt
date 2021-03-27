package org.kryptonmc.krypton.command.commands

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.command.Command
import org.kryptonmc.krypton.api.command.Sender
import kotlin.system.exitProcess

class StopCommand : Command("stop", "krypton.command.stop") {

    override suspend fun execute(sender: Sender, args: List<String>) {
        sender.sendMessage(Component.text("Stopping server..."))
        exitProcess(0)
    }
}
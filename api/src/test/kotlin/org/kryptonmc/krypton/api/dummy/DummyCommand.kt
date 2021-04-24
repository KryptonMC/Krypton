package org.kryptonmc.krypton.api.dummy

import org.kryptonmc.krypton.api.command.Command
import org.kryptonmc.krypton.api.command.Sender

class DummyCommand(
    name: String,
    permission: String? = null,
    aliases: List<String> = emptyList()
) : Command(name, permission, aliases) {

    override fun execute(sender: Sender, args: List<String>) = Unit
}

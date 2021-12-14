package org.kryptonmc.krypton.commands.krypton

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.literal

object KryptonCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("krypton") {
            val pluginsNode = PluginsCommand.register().build()
            then(pluginsNode)
            PluginsCommand.aliases.forEach { then(LiteralArgumentBuilder.literal<Sender>(it).redirect(pluginsNode)) }
            val versionNode = InfoCommand.register().build()
            then(versionNode)
            InfoCommand.aliases.forEach { then(LiteralArgumentBuilder.literal<Sender>(it).redirect(versionNode)) }
        })
    }
}

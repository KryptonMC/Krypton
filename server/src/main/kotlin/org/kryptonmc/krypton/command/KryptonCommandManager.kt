package org.kryptonmc.krypton.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.CommandSyntaxException
import kotlinx.coroutines.launch
import me.bardy.admiral.literal
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.api.command.Command
import org.kryptonmc.krypton.api.command.CommandManager
import org.kryptonmc.krypton.api.command.Sender

class KryptonCommandManager : CommandManager {

    internal val dispatcher = CommandDispatcher<Sender>()

    override fun register(command: Command) {
        val commandNode = dispatcher.register(literal(command.name) {
            executes {
                val sender = it.source
                val args = it.input.split(" ").drop(0)
                val permission = command.permission ?: return@executes dispatchCommand(command, sender, args)

                if (sender.hasPermission(permission)) return@executes dispatchCommand(command, sender, args)
                0
            }
        })

        command.aliases.forEach { dispatcher.register(literal(it) { redirect(commandNode) }) }
    }

    override fun register(vararg commands: Command) = commands.forEach { register(it) }

    override fun register(commands: Iterable<Command>) = commands.forEach { register(it) }

    override fun register(command: LiteralArgumentBuilder<Sender>) {
        dispatcher.register(command)
    }

    override fun register(vararg commands: LiteralArgumentBuilder<Sender>) = commands.forEach { register(it) }

    override fun dispatch(sender: Sender, command: String) {
        try {
            if (dispatcher.execute(command, sender) != 1) sender.sendMessage(DEFAULT_NO_PERMISSION)
        } catch (exception: CommandSyntaxException) {
            sender.sendMessage(Component.text("Unknown command. Type help for help."))
        }
    }

    private fun dispatchCommand(command: Command, sender: Sender, args: List<String>): Int {
        CommandScope.launch { command.execute(sender, args) }
        return 1
    }

    companion object {

        private val DEFAULT_NO_PERMISSION = Component.text("You do not have permission to execute this command!")
            .color(NamedTextColor.RED)
    }
}
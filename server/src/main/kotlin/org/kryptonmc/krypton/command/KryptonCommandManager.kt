package org.kryptonmc.krypton.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.CommandSyntaxException.BUILT_IN_EXCEPTIONS
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.command.Command
import org.kryptonmc.krypton.api.command.CommandManager
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.event.events.play.PermissionCheckEvent
import org.kryptonmc.krypton.api.event.events.play.PermissionCheckResult
import org.kryptonmc.krypton.extension.logger

class KryptonCommandManager(private val server: KryptonServer) : CommandManager {

    internal val dispatcher = CommandDispatcher<Sender>()

    override fun register(command: Command) {
        val commandNode = dispatcher.register(
            literal<Sender>(command.name)
                .executes { execute(command, it) }
                .then(argument<Sender, String>("args", greedyString()).executes { execute(command, it) })
        )

        command.aliases.forEach { dispatcher.register(literal<Sender>(it).redirect(commandNode)) }
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
            val message = when (exception) {
                BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand() -> Component.text("Unknown command $command.")
                else -> Component.text(exception.message ?: "")
            }
            sender.sendMessage(message)
        }
    }

    private fun dispatchCommand(command: Command, sender: Sender, args: List<String>): Int {
        CommandScope.launch { command.execute(sender, args) }
        return 1
    }

    private fun dispatchPermissionCheck(sender: Sender, permission: String?): PermissionCheckResult {
        val event = PermissionCheckEvent(sender, permission, true)
        server.eventBus.call(event)
        return event.result
    }

    private fun execute(command: Command, context: CommandContext<Sender>): Int {
        val sender = context.source
        val args = context.input.split(" ").drop(1)

        if (command.permission == null) {
            dispatchPermissionCheck(sender, null)
            return dispatchCommand(command, sender, args)
        }

        return when (dispatchPermissionCheck(sender, command.permission)) {
            PermissionCheckResult.TRUE -> dispatchCommand(command, sender, args)
            PermissionCheckResult.FALSE -> 0
            PermissionCheckResult.UNSET -> 0
        }
    }

    companion object {

        private val DEFAULT_NO_PERMISSION = Component.text("You do not have permission to execute this command!")
            .color(NamedTextColor.RED)
    }
}
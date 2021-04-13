package org.kryptonmc.krypton.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.ParseResults
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.CommandSyntaxException.BUILT_IN_EXCEPTIONS
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.command.Command
import org.kryptonmc.krypton.api.command.CommandManager
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.event.events.play.PermissionCheckEvent
import org.kryptonmc.krypton.api.event.events.play.PermissionCheckResult
import org.kryptonmc.krypton.command.commands.StopCommand
import java.util.*
import java.util.concurrent.CompletableFuture

class KryptonCommandManager(private val server: KryptonServer) : CommandManager {

    internal val dispatcher = CommandDispatcher<Sender>()

    override fun register(command: Command) {
        val commandNode = buildRawArgumentsLiteral(
            command.name,
            { execute(command, it) },
            { context, builder -> suggest(command, context, builder) }
        )
        dispatcher.root.addChild(commandNode)

        command.aliases.forEach { dispatcher.root.addChild(commandNode.buildRedirect(it)) }
    }

    override fun register(vararg commands: Command) = commands.forEach { register(it) }

    override fun register(commands: Iterable<Command>) = commands.forEach { register(it) }

    override fun register(command: LiteralCommandNode<Sender>) = dispatcher.root.addChild(command)

    override fun register(vararg commands: LiteralCommandNode<Sender>) = commands.forEach { register(it) }

    override fun dispatch(sender: Sender, command: String) {
        try {
            if (dispatcher.execute(command.removePrefix("/"), sender) != 1) sender.sendMessage(DEFAULT_NO_PERMISSION)
        } catch (exception: CommandSyntaxException) {
            val message = when (exception) {
                BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand() -> Component.text("Unknown command $command.")
                else -> Component.text(exception.message ?: "")
            }
            sender.sendMessage(message)
        }
    }

    /**
     * Retrieves command completion suggestions from the specified parse results
     */
    fun suggest(parseResults: ParseResults<Sender>): CompletableFuture<Suggestions> =
        dispatcher.getCompletionSuggestions(parseResults)

    private fun dispatchCommand(command: Command, sender: Sender, args: List<String>): Int {
        CommandScope.launch { command.execute(sender, args) }
        return 1
    }

    private fun dispatchPermissionCheck(sender: Sender, permission: String?): PermissionCheckResult {
        val event = PermissionCheckEvent(sender, permission, permission?.let { sender.hasPermission(it) } ?: true)
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

    private fun suggest(
        command: Command,
        context: CommandContext<Sender>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val args = context.input.split(" ").drop(1)
        val hasPermission = command.permission?.let(context.source::hasPermission) ?: true
        if (!hasPermission) return builder.buildFuture()

        command.suggest(context.source, args).forEach { builder.suggest(it) }
        return builder.buildFuture()
    }

    private fun LiteralCommandNode<Sender>.buildRedirect(alias: String) =
        literal<Sender>(alias.toLowerCase(Locale.ENGLISH))
            .requires(requirement)
            .forward(redirect, redirectModifier, isFork)
            .executes(command)
            .apply { children.forEach { then(it) } }
            .build()

    private fun buildRawArgumentsLiteral(
        alias: String,
        brigadierCommand: com.mojang.brigadier.Command<Sender>,
        suggestionProvider: SuggestionProvider<Sender>
    ) = literal<Sender>(alias.toLowerCase(Locale.ENGLISH))
        .then(argument<Sender, String>("arguments", greedyString())
            .suggests(suggestionProvider)
            .executes(brigadierCommand))
        .executes(brigadierCommand)
        .build()

    internal fun registerBuiltins() {
        register(StopCommand())
    }

    companion object {

        private val DEFAULT_NO_PERMISSION = Component.text("You do not have permission to execute this command!")
            .color(NamedTextColor.RED)
    }
}
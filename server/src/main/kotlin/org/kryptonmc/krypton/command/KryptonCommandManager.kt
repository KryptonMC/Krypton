/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.ParseResults
import com.mojang.brigadier.ResultConsumer
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.tree.RootCommandNode
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.adventure.AdventureMessage
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.CommandManager
import org.kryptonmc.api.command.InvocableCommand
import org.kryptonmc.api.command.RawCommand
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.command.SimpleCommand
import org.kryptonmc.api.command.CommandMeta
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.commands.ClearCommand
import org.kryptonmc.krypton.commands.DifficultyCommand
import org.kryptonmc.krypton.commands.GameModeCommand
import org.kryptonmc.krypton.commands.GameRuleCommand
import org.kryptonmc.krypton.commands.GiveCommand
import org.kryptonmc.krypton.commands.KickCommand
import org.kryptonmc.krypton.commands.ListCommand
import org.kryptonmc.krypton.commands.MeCommand
import org.kryptonmc.krypton.commands.MessageCommand
import org.kryptonmc.krypton.commands.SayCommand
import org.kryptonmc.krypton.commands.SeedCommand
import org.kryptonmc.krypton.commands.StopCommand
import org.kryptonmc.krypton.commands.SummonCommand
import org.kryptonmc.krypton.commands.TeleportCommand
import org.kryptonmc.krypton.commands.TitleCommand
import org.kryptonmc.krypton.command.registrar.BrigadierCommandRegistrar
import org.kryptonmc.krypton.command.registrar.RawCommandRegistrar
import org.kryptonmc.krypton.command.registrar.SimpleCommandRegistrar
import org.kryptonmc.krypton.commands.krypton.KryptonCommand
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.event.player.KryptonPlayerUpdateCommandsEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutCommands
import org.kryptonmc.krypton.util.downcastApiType
import java.util.concurrent.CompletableFuture
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.annotation.concurrent.GuardedBy
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.math.max
import kotlin.math.min

class KryptonCommandManager : CommandManager {

    @GuardedBy("lock")
    private val dispatcher = CommandDispatcher<Source>() // Reads and writes MUST be locked by this lock!
    private val lock = ReentrantReadWriteLock()

    private val brigadierCommandRegistrar = BrigadierCommandRegistrar(lock.writeLock())
    private val simpleCommandRegistrar = SimpleCommandRegistrar(lock.writeLock())
    private val rawCommandRegistrar = RawCommandRegistrar(lock.writeLock())

    override fun register(command: BrigadierCommand, meta: CommandMeta) {
        brigadierCommandRegistrar.register(dispatcher.root, command, meta)
    }

    override fun register(command: InvocableCommand<*>, meta: CommandMeta) {
        when (command) {
            is SimpleCommand -> simpleCommandRegistrar.register(dispatcher.root, command, meta)
            is RawCommand -> rawCommandRegistrar.register(dispatcher.root, command, meta)
        }
    }

    override fun unregister(alias: String) {
        lock.write { dispatcher.root.removeChildByName(alias.lowercase()) }
    }

    override fun dispatch(sender: Sender, command: String): Boolean =
        dispatch(sender.downcast().createCommandSourceStack(), command, NO_OP_RESULT_CONSUMER)

    fun dispatch(source: Source, command: String): Boolean = dispatch(source, command, NO_OP_RESULT_CONSUMER)

    fun dispatch(source: Source, command: String, resultCallback: ResultConsumer<Source>): Boolean {
        val normalized = normalizeInput(command)
        return try {
            val parseResults = parse(source, normalized)
            dispatcher.execute(parseResults, resultCallback)
            true
        } catch (exception: CommandSyntaxException) {
            // The exception formatting here is mostly based on that of vanilla, so we can actually report all of the useful
            // information that we may want for exception messages thrown by commands.
            val rawMessage = exception.rawMessage
            val message = if (rawMessage is AdventureMessage) rawMessage.asComponent() else Component.text(exception.message.orEmpty())
            source.sendFailure(message.color(NamedTextColor.RED))

            // This will process extra stuff that we want for proper error reporting to clients.
            if (exception.input != null && exception.cursor >= 0) {
                val inputLength = min(exception.input.length, exception.cursor)
                val errorMessage = Component.text().style {
                    it.color(NamedTextColor.GRAY)
                    it.clickEvent(ClickEvent.suggestCommand(command))
                }

                // If the length of the input is too long, we shorten it by appending ... at the beginning.
                if (inputLength > ERROR_MESSAGE_CUTOFF_THRESHOLD) errorMessage.append(Component.text("..."))
                errorMessage.append(Component.text(exception.input.substring(max(0, inputLength - ERROR_MESSAGE_CUTOFF_THRESHOLD), inputLength)))

                if (inputLength < exception.input.length) {
                    errorMessage.append(Component.text(exception.input.substring(inputLength), NamedTextColor.RED, TextDecoration.UNDERLINED))
                }

                // Append the "[HERE]" text (locale-specific) to the end, just like vanilla.
                errorMessage.append(Component.translatable("command.context.here", NamedTextColor.RED, TextDecoration.ITALIC))
                source.sendFailure(Component.text().append(errorMessage).color(NamedTextColor.RED).build())
            }
            false
        } catch (naughty: Throwable) { // We catch Throwable because plugins like to do stupid things sometimes.
            LOGGER.error("Unable to dispatch command $command from $source!", naughty)
            throw RuntimeException("Unable to dispatch command $command from $source!", naughty)
        }
    }

    fun suggest(results: ParseResults<Source>, cursor: Int): CompletableFuture<Suggestions> = dispatcher.getCompletionSuggestions(results, cursor)

    fun suggest(results: ParseResults<Source>): CompletableFuture<Suggestions> = dispatcher.getCompletionSuggestions(results)

    override fun updateCommands(player: Player) {
        if (player !is KryptonPlayer) return
        // We copy the root node to avoid a command changing whilst we're trying to send it to the client.
        val node = RootCommandNode<CommandSourceStack>()
        lock.read {
            dispatcher.root.children.forEach { if (it.requirement.test(player.createCommandSourceStack())) node.addChild(it) }
        }
        player.server.eventNode.fire(KryptonPlayerUpdateCommandsEvent(player, node))
        player.connection.send(PacketOutCommands.createFromRootNode(node))
    }

    fun registerBuiltins() {
        StopCommand.register(dispatcher)
        TeleportCommand.register(dispatcher)
        SummonCommand.register(dispatcher)
        GameModeCommand.register(dispatcher)
        ListCommand.register(dispatcher)
        SeedCommand.register(dispatcher)
        SayCommand.register(dispatcher)
        MeCommand.register(dispatcher)
        MessageCommand.register(dispatcher)
        TitleCommand.register(dispatcher)
        DifficultyCommand.register(dispatcher)
        GameRuleCommand.register(dispatcher)
        KickCommand.register(dispatcher)
        GiveCommand.register(dispatcher)
        ClearCommand.register(dispatcher)
        KryptonCommand.register(dispatcher)
    }

    fun parse(sender: Source, input: String): ParseResults<Source> = lock.read { dispatcher.parse(input, sender) }

    fun parse(sender: Source, reader: StringReader): ParseResults<Source> = lock.read { dispatcher.parse(reader, sender) }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private const val ERROR_MESSAGE_CUTOFF_THRESHOLD = 10
        private val NO_OP_RESULT_CONSUMER = ResultConsumer<Source> { _, _, _ ->}

        @JvmStatic
        private fun normalizeInput(input: String): String {
            val command = input.trim()
            val firstSeparator = command.indexOf(CommandDispatcher.ARGUMENT_SEPARATOR_CHAR)
            if (firstSeparator != -1) return command.substring(0, firstSeparator).lowercase() + command.substring(firstSeparator)
            return command.lowercase()
        }
    }
}

private typealias Source = CommandSourceStack

private fun Sender.downcast(): KryptonSender = downcastApiType("Sender")

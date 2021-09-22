/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.ParseResults
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.tree.RootCommandNode
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.kryptonmc.api.adventure.AdventureMessage
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.Command
import org.kryptonmc.api.command.CommandManager
import org.kryptonmc.api.command.CommandRegistrar
import org.kryptonmc.api.command.RawCommand
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.command.SimpleCommand
import org.kryptonmc.api.command.meta.CommandMeta
import org.kryptonmc.api.command.meta.SimpleCommandMeta
import org.kryptonmc.krypton.commands.BanCommand
import org.kryptonmc.krypton.commands.BanIpCommand
import org.kryptonmc.krypton.commands.ClearCommand
import org.kryptonmc.krypton.commands.DeopCommand
import org.kryptonmc.krypton.commands.DifficultyCommand
import org.kryptonmc.krypton.commands.GameModeCommand
import org.kryptonmc.krypton.commands.GameruleCommand
import org.kryptonmc.krypton.commands.GiveCommand
import org.kryptonmc.krypton.commands.KickCommand
import org.kryptonmc.krypton.commands.ListCommand
import org.kryptonmc.krypton.commands.MeCommand
import org.kryptonmc.krypton.commands.MessageCommand
import org.kryptonmc.krypton.commands.OpCommand
import org.kryptonmc.krypton.commands.PardonCommand
import org.kryptonmc.krypton.commands.PardonIpCommand
import org.kryptonmc.krypton.commands.RestartCommand
import org.kryptonmc.krypton.commands.SayCommand
import org.kryptonmc.krypton.commands.SeedCommand
import org.kryptonmc.krypton.commands.StopCommand
import org.kryptonmc.krypton.commands.SummonCommand
import org.kryptonmc.krypton.commands.TeleportCommand
import org.kryptonmc.krypton.commands.TitleCommand
import org.kryptonmc.krypton.commands.VersionCommand
import org.kryptonmc.krypton.commands.WhitelistCommand
import org.kryptonmc.krypton.command.registrar.BrigadierCommandRegistrar
import org.kryptonmc.krypton.command.registrar.RawCommandRegistrar
import org.kryptonmc.krypton.command.registrar.SimpleCommandRegistrar
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutDeclareCommands
import java.util.concurrent.CompletableFuture
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.annotation.concurrent.GuardedBy
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.math.max
import kotlin.math.min

class KryptonCommandManager : CommandManager {

    @GuardedBy("lock") val dispatcher = CommandDispatcher<Sender>() // Reads and writes MUST be locked by the lock below!
    private val lock = ReentrantReadWriteLock()
    private val brigadierCommandRegistrar = BrigadierCommandRegistrar(lock.writeLock())
    private val simpleCommandRegistrar = SimpleCommandRegistrar(lock.writeLock())
    private val rawCommandRegistrar = RawCommandRegistrar(lock.writeLock())

    override fun register(command: BrigadierCommand) {
        brigadierCommandRegistrar.register(dispatcher.root, command, CommandMeta.builder(command).build())
    }

    override fun register(command: SimpleCommand, meta: SimpleCommandMeta) {
        simpleCommandRegistrar.register(dispatcher.root, command, meta)
    }

    override fun register(command: RawCommand, meta: CommandMeta) {
        rawCommandRegistrar.register(dispatcher.root, command, meta)
    }

    override fun <C : Command, M : CommandMeta> register(command: C, meta: M, registrar: CommandRegistrar<C, M>) {
        lock.write { registrar.register(dispatcher.root, command, meta) }
    }

    override fun unregister(alias: String) = dispatcher.root.removeChildByName(alias)

    override fun dispatch(sender: Sender, command: String): Boolean {
        val normalized = command.normalize(true)
        return try {
            val parseResults = parse(sender, normalized)
            dispatcher.execute(parseResults) != BrigadierCommand.FORWARD
        } catch (exception: CommandSyntaxException) {
            val rawMessage = exception.rawMessage
            val message = if (rawMessage is AdventureMessage) rawMessage.wrapped else text(exception.message.orEmpty())
            sender.sendMessage(message.color(NamedTextColor.RED))

            // This will process extra stuff that we want for proper error reporting to clients.
            if (exception.input != null && exception.cursor >= 0) {
                val inputLength = min(exception.input.length, exception.cursor)
                var errorMessage = text("", Style.style(NamedTextColor.GRAY)
                    .clickEvent(ClickEvent.suggestCommand(command)))

                // If the length of the input is too long, we shorten it by appending ... at the beginning.
                if (inputLength > 10) errorMessage = errorMessage.append(text("..."))
                errorMessage = errorMessage.append(
                    text(exception.input.substring(max(0, inputLength - 10), inputLength))
                )

                if (inputLength < exception.input.length) {
                    val error = text(
                        exception.input.substring(inputLength),
                        NamedTextColor.RED,
                        TextDecoration.UNDERLINED
                    )
                    errorMessage = errorMessage.append(error)
                }

                // Append the "[HERE]" text (locale-specific) to the end, just like vanilla.
                errorMessage = errorMessage.append(translatable(
                    "command.context.here",
                    NamedTextColor.RED,
                    TextDecoration.ITALIC
                ))
                sender.sendMessage(text("").append(errorMessage).color(NamedTextColor.RED))
            }
            false
        } catch (exception: Throwable) { // We catch Throwable because plugins like to do stupid things sometimes.
            throw RuntimeException("Unable to dispatch command $command from $sender!", exception)
        }
    }

    fun suggest(parseResults: ParseResults<Sender>): CompletableFuture<Suggestions> =
        dispatcher.getCompletionSuggestions(parseResults)

    fun sendCommands(player: KryptonPlayer) {
        val node = RootCommandNode<Sender>()
        lock.read {
            dispatcher.root.children.forEach { if (it.requirement.test(player)) node.addChild(it) }
            player.session.send(PacketOutDeclareCommands(node))
        }
    }

    // Registers all the built-in commands.
    fun registerBuiltins() {
        StopCommand.register(dispatcher)
        RestartCommand.register(dispatcher)
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
        GameruleCommand.register(dispatcher)
        VersionCommand.register(dispatcher)
        KickCommand.register(dispatcher)
        BanCommand.register(dispatcher)
        PardonCommand.register(dispatcher)
        WhitelistCommand.register(dispatcher)
        BanIpCommand.register(dispatcher)
        PardonIpCommand.register(dispatcher)
        OpCommand.register(dispatcher)
        DeopCommand.register(dispatcher)
        GiveCommand.register(dispatcher)
        ClearCommand.register(dispatcher)
    }

    private fun parse(sender: Sender, input: String): ParseResults<Sender> = lock.read {
        dispatcher.parse(input, sender)
    }
}

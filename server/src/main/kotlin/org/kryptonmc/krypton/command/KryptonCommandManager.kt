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
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.commands.BanCommand
import org.kryptonmc.krypton.command.commands.BanIpCommand
import org.kryptonmc.krypton.command.commands.DebugCommand
import org.kryptonmc.krypton.command.commands.DeopCommand
import org.kryptonmc.krypton.command.commands.DifficultyCommand
import org.kryptonmc.krypton.command.commands.GamemodeCommand
import org.kryptonmc.krypton.command.commands.GameruleCommand
import org.kryptonmc.krypton.command.commands.KickCommand
import org.kryptonmc.krypton.command.commands.ListCommand
import org.kryptonmc.krypton.command.commands.MeCommand
import org.kryptonmc.krypton.command.commands.MessageCommand
import org.kryptonmc.krypton.command.commands.OpCommand
import org.kryptonmc.krypton.command.commands.PardonCommand
import org.kryptonmc.krypton.command.commands.PardonIpCommand
import org.kryptonmc.krypton.command.commands.RestartCommand
import org.kryptonmc.krypton.command.commands.SayCommand
import org.kryptonmc.krypton.command.commands.SeedCommand
import org.kryptonmc.krypton.command.commands.StopCommand
import org.kryptonmc.krypton.command.commands.SummonCommand
import org.kryptonmc.krypton.command.commands.TeleportCommand
import org.kryptonmc.krypton.command.commands.TitleCommand
import org.kryptonmc.krypton.command.commands.VersionCommand
import org.kryptonmc.krypton.command.commands.WhitelistCommand
import org.kryptonmc.krypton.command.registrar.BrigadierCommandRegistrar
import org.kryptonmc.krypton.command.registrar.RawCommandRegistrar
import org.kryptonmc.krypton.command.registrar.SimpleCommandRegistrar
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutDeclareCommands
import java.util.concurrent.CompletableFuture
import java.util.concurrent.locks.ReentrantReadWriteLock

class KryptonCommandManager(server: KryptonServer) : CommandManager {

    internal val dispatcher = CommandDispatcher<Sender>()
    private val lock = ReentrantReadWriteLock()
    private val brigadierCommandRegistrar = BrigadierCommandRegistrar(lock.writeLock())
    private val simpleCommandRegistrar = SimpleCommandRegistrar(server, lock.writeLock())
    private val rawCommandRegistrar = RawCommandRegistrar(lock.writeLock())

    override fun register(command: BrigadierCommand) = brigadierCommandRegistrar.register(dispatcher.root, command, CommandMeta.builder(command).build())

    override fun register(command: SimpleCommand, meta: SimpleCommandMeta) = simpleCommandRegistrar.register(dispatcher.root, command, meta)

    override fun register(command: RawCommand, meta: CommandMeta) = rawCommandRegistrar.register(dispatcher.root, command, meta)

    override fun <C : Command, M : CommandMeta> register(command: C, meta: M, registrar: CommandRegistrar<C, M>) {
        lock.writeLock().lock()
        try {
            registrar.register(dispatcher.root, command, meta)
        } finally {
            lock.writeLock().unlock()
        }
    }

    override fun unregister(alias: String) = dispatcher.root.removeChildByName(alias)

    override fun dispatch(sender: Sender, command: String): Boolean {
        val normalized = command.normalize(true)
        return try {
            val parseResults = parse(sender, normalized)
            dispatcher.execute(parseResults) != BrigadierCommand.FORWARD
        } catch (exception: CommandSyntaxException) {
            val message = exception.rawMessage
            sender.sendMessage(if (message is AdventureMessage) message.wrapped else text(exception.message.orEmpty()))
            false
        } catch (exception: Throwable) {
            // Catch Throwable because plugins like to do stupid things sometimes
            throw RuntimeException("Unable to dispatch command $command from $sender!", exception)
        }
    }

    fun suggest(parseResults: ParseResults<Sender>): CompletableFuture<Suggestions> = dispatcher.getCompletionSuggestions(parseResults)

    fun sendCommands(player: KryptonPlayer) {
        val node = RootCommandNode<Sender>()
        lock.readLock().lock()
        try {
            dispatcher.root.children.forEach { if (it.requirement.test(player)) node.addChild(it) }
            player.session.sendPacket(PacketOutDeclareCommands(node))
        } finally {
            lock.readLock().unlock()
        }
    }

    internal fun registerBuiltins() {
        StopCommand.register(dispatcher)
        RestartCommand.register(dispatcher)
        DebugCommand.register(dispatcher)
        TeleportCommand.register(dispatcher)
        SummonCommand.register(dispatcher)
        GamemodeCommand.register(dispatcher)
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
    }

    private fun parse(sender: Sender, input: String): ParseResults<Sender> {
        lock.readLock().lock()
        return try {
            dispatcher.parse(input, sender)
        } finally {
            lock.readLock().unlock()
        }
    }
}

package org.kryptonmc.krypton.api

import com.mojang.brigadier.tree.LiteralCommandNode
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.runs
import io.mockk.spyk
import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.api.command.Command
import org.kryptonmc.krypton.api.command.CommandManager
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.dummy.DummyCommand
import org.kryptonmc.krypton.api.event.EventBus
import org.kryptonmc.krypton.api.plugin.Plugin
import org.kryptonmc.krypton.api.plugin.PluginContext
import org.kryptonmc.krypton.api.plugin.PluginDescriptionFile
import java.io.File

val commandManagerMock = mockk<CommandManager> {
    every { register(any<Command>()) } returns Unit
    every { register(*anyVararg<Command>()) } returns Unit
    every { register(any<Iterable<Command>>()) } returns Unit
    every { register(any<LiteralCommandNode<Sender>>()) } returns Unit
    every { register(*anyVararg<LiteralCommandNode<Sender>>()) } returns Unit
}

val eventBusMock = mockk<EventBus> {
    every { call(any()) } returns Unit
    every { register(any()) } returns Unit
}

val server = mockk<Server> {
    every { commandManager } returns commandManagerMock
    every { eventBus } returns eventBusMock
}

val command = spyk(DummyCommand("test", "test.test", listOf("hello", "world"))) {
    every { execute(any(), any()) } returns Unit
    every { suggest(any(), any()) } returns listOf("suggest", "me")
}

val sender = mockk<Sender> {
    every { name } returns "Dave"
    every { permissions } returns emptyMap()
}

val description = PluginDescriptionFile(
    "Test",
    "org.kryptonmc.test.TestPlugin",
    "1.1.1",
    "I am a test plugin!",
    listOf("BomBardyGamer"),
    listOf("EventTester")
)

val logger = mockk<Logger> {
    every { info(any<String>()) } just runs
    every { warn(any<String>()) } just runs
    every { error(any<String>()) } just runs
}

val plugin = object : Plugin(PluginContext(server, File(""), description, logger)) {}

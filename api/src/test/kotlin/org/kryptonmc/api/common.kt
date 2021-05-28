/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.command.SimpleCommand
import org.kryptonmc.api.command.CommandManager
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.dummy.DummyCommand
import org.kryptonmc.api.entity.entities.Player
import org.kryptonmc.api.event.EventBus
import org.kryptonmc.api.plugin.Plugin
import org.kryptonmc.api.plugin.PluginContext
import org.kryptonmc.api.plugin.PluginDescriptionFile
import java.net.InetSocketAddress
import java.nio.file.Path
import java.util.UUID

val commandManagerMock = mockk<CommandManager> {
    every { register(any<SimpleCommand>()) } returns Unit
}

val eventBusMock = mockk<EventBus> {
    every { call(any()) } returns Unit
    every { register(any()) } returns Unit
}

val server = mockk<Server> {
    every { commandManager } returns commandManagerMock
    every { eventBus } returns eventBusMock
}

val command = DummyCommand("test", "test.test", listOf("hello", "world"))

val sender = mockk<Sender> {
    every { name } returns "Dave"
    every { permissions } returns emptyMap()
}

val player = mockk<Player> {
    every { name } returns "Dorothy"
    every { permissions } returns emptyMap()
    every { uuid } returns UUID.randomUUID()
    every { address } returns InetSocketAddress.createUnresolved("0.0.0.0", 25565)
    every { sendMessage(any()) } just runs
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

val plugin = object : Plugin() {}

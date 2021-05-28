/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

/**
 * A command that can be sent by a [Sender], such as a [org.kryptonmc.api.entity.entities.Player]
 * or the [ConsoleSender].
 *
 * This interface cannot be inherited from directly. You must use one of the registrable interfaces:
 *
 * * [BrigadierCommand] - the more modern type of command, backed by Brigadier's [com.mojang.brigadier.tree.LiteralCommandNode]
 * * [SimpleCommand] - the older style of command, mainly popularised by Bukkit and BungeeCord.
 * * [RawCommand] - provides everything as-is, so it can be processed by external frameworks.
 */
sealed interface Command

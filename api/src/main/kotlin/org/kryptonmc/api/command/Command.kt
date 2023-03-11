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
package org.kryptonmc.api.command

/**
 * A command that can be sent by a [Sender], such as a
 * [player][org.kryptonmc.api.entity.player.Player] or the [ConsoleSender].
 *
 * There are three built-in types that inherit from this base interface:
 *
 * * [BrigadierCommand] - the more modern type of command, backed by
 * Brigadier's [com.mojang.brigadier.tree.LiteralCommandNode]
 * * [SimpleCommand] - the older style of command, mainly popularised by Bukkit
 * and BungeeCord.
 * * [RawCommand] - provides everything as-is, so it can be processed by
 * external frameworks.
 */
public sealed interface Command

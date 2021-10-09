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
 * A command styled after the convention popularised by Bukkit and BungeeCord.
 *
 * It is highly recommended to use the new Brigadier command system.
 * See [BrigadierCommand].
 *
 * @see BrigadierCommand
 */
public fun interface SimpleCommand : InvocableCommand<Array<String>>

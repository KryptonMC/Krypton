/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.dummy

import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.command.SimpleCommand

class DummyCommand(
    name: String,
    permission: String? = null,
    aliases: List<String> = emptyList()
) : SimpleCommand(name, permission, aliases) {

    override fun execute(sender: Sender, args: Array<String>) = Unit
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.dummy

import org.kryptonmc.api.command.Command
import org.kryptonmc.api.command.Sender

class DummyCommand(
    name: String,
    permission: String? = null,
    aliases: List<String> = emptyList()
) : Command(name, permission, aliases) {

    override fun execute(sender: Sender, args: List<String>) = Unit
}

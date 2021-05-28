/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import net.kyori.adventure.identity.Identity
import org.kryptonmc.api.command.Sender

/**
 * The console sender represents the server source. That is, as if the server sent the command.
 *
 * There are some defaults set in here to make it easier for implementations, and also to allow users
 * to more accurately see how it might work.
 *
 * The console's name should always be "CONSOLE", it should always use the special nil UUID
 * (a UUID comprised of all zeros). It should never fail permission checks, and it should not
 * store permissions, or succeed in granting or revoking them.
 */
interface ConsoleSender : Sender {

    override val name: String
        get() = "CONSOLE"

    override val permissions: Map<String, Boolean>
        get() = emptyMap()

    override fun hasPermission(permission: String) = true

    override fun grant(permission: String) = Unit

    override fun revoke(permission: String) = Unit

    override fun identity() = Identity.nil()
}

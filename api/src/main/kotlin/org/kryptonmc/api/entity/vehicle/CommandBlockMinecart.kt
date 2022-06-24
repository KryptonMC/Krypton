/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.vehicle

import net.kyori.adventure.text.Component

/**
 * A minecart with a command block in it.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface CommandBlockMinecart : MinecartLike {

    /**
     * The command that this command block Minecart will run.
     */
    @get:JvmName("command")
    public var command: String

    /**
     * The last output from the command block running the command.
     */
    @get:JvmName("lastOutput")
    public var lastOutput: Component
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import net.kyori.adventure.text.Component

/**
 * A command block.
 */
public interface CommandBlock : BlockEntity {

    /**
     * The command that this command block will run.
     */
    public var command: String

    /**
     * The last output from the command block running the command.
     */
    public var lastOutput: Component

    /**
     * Whether this command block is powered.
     */
    public val isPowered: Boolean

    /**
     * Whether this command block executes automatically, not requiring a
     * redstone signal.
     */
    public val isAutomatic: Boolean

    /**
     * Executes this command block.
     */
    public fun execute()
}

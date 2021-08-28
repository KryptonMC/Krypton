/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode

/**
 * A command that is backed by a Brigadier [LiteralCommandNode].
 *
 * @param node the node that backs this command
 */
class BrigadierCommand(val node: LiteralCommandNode<Sender>) : Command {

    /**
     * Constructs a command that is backed by a Brigadier [LiteralCommandNode] from
     * the node's builder type [LiteralArgumentBuilder].
     *
     * @param builder the builder to build the backing node from
     */
    constructor(builder: LiteralArgumentBuilder<Sender>) : this(builder.build())

    companion object {

        /**
         * The return code used by Brigadier to indicate that command execution
         * should be forwarded to the backend server.
         */
        const val FORWARD = 0xF6287429.toInt()
    }
}

/**
 * Builds a command backed by a Brigadier [LiteralCommandNode].
 *
 * @param literal the primary command name
 * @param builder the builder to build the command with
 * @return the built Brigadier command
 */
@JvmSynthetic
inline fun brigadierCommand(
    literal: String,
    builder: LiteralArgumentBuilder<Sender>.() -> Unit
): BrigadierCommand = BrigadierCommand(LiteralArgumentBuilder.literal<Sender>(literal).apply(builder).build())

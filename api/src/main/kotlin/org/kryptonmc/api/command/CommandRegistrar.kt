/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import com.mojang.brigadier.tree.RootCommandNode
import org.kryptonmc.api.command.meta.CommandMeta

/**
 * A registrar of commands. Implement this to define how the server should register commands
 * of type [C] with metadata of type [M]
 *
 * @param C the command type
 * @param M the metadata type
 */
fun interface CommandRegistrar<C : Command, M : CommandMeta> {

    /**
     * Registers the given [command] with the given [meta] to the given
     * [root] command node.
     *
     * @param root the root command node to register to
     * @param command the command to register
     * @param meta the command metadata
     */
    fun register(root: RootCommandNode<Sender>, command: C, meta: M)
}

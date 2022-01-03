/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import com.mojang.brigadier.tree.RootCommandNode

/**
 * A registrar of commands. Implement this to define how the server should
 * register commands of type [C] with metadata of type [M].
 *
 * This is designed for external command frameworks, to give them even more
 * control over registration of commands than what is otherwise provided.
 * It is also to discourage the use of wrapper classes for delegating calls.
 *
 * @param C the command type
 * @param M the metadata type
 */
public fun interface CommandRegistrar<C, M> {

    /**
     * Registers the given [command] with the given [meta] to the given [root]
     * command node.
     *
     * This will be invoked when commands of type [C] are registered to the
     * command manager with [CommandManager.register].
     *
     * @param root the root command node to register to
     * @param command the command to register
     * @param meta the command metadata
     */
    public fun register(root: RootCommandNode<Sender>, command: C, meta: M)
}

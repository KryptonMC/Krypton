/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.command

import com.mojang.brigadier.tree.RootCommandNode
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player

/**
 * Called when the given [rootNode] is sent to the given [player].
 */
public interface CommandSendEvent {

    /**
     * The player the commands are being sent to.
     */
    public val player: Player

    /**
     * The root of the command tree.
     *
     * This can be mutated to change the command tree that is sent to the
     * player.
     */
    public val rootNode: RootCommandNode<Sender>
}

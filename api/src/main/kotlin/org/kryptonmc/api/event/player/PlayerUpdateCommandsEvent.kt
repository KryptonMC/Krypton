/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import com.mojang.brigadier.tree.RootCommandNode
import org.kryptonmc.api.event.type.PlayerEvent

/**
 * Called when the server updates the available commands for the player.
 */
public interface PlayerUpdateCommandsEvent : PlayerEvent {

    /**
     * The root of the command tree that will be sent.
     *
     * This can be mutated to change the command tree that is sent to the
     * player.
     */
    public val rootNode: RootCommandNode<*>
}

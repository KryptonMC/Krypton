/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
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
 *
 * Note: the root node given here can be mutated.
 *
 * @param player the player the commands are being sent to
 * @param rootNode the root of the command node tree
 */
@JvmRecord
public data class CommandSendEvent(
    public val player: Player,
    public val rootNode: RootCommandNode<Sender>
)

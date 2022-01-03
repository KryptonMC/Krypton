/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.resource.ResourcePack

/**
 * Called when the given [player] updates the server about the status of the
 * resource pack that the server has sent them.
 *
 * @param player the player
 * @param status the status the client reported
 */
@JvmRecord
public data class ResourcePackStatusEvent(
    public val player: Player,
    public val status: ResourcePack.Status
)

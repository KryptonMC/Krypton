/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.resource.ResourcePack

/**
 * Called when the given [player] updates the server about the status of the
 * resource pack that the server has sent them.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ResourcePackStatusEvent : PlayerEvent {

    /**
     * The status of the player's resource pack.
     */
    @get:JvmName("status")
    public val status: ResourcePack.Status
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.internal.annotations.ImmutableType
import java.util.Locale

/**
 * The settings for a player indicated by the client.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface PlayerSettings {

    /**
     * The locale of the player.
     *
     * If this has not been specified by the client, this will be null.
     */
    @get:JvmName("locale")
    public val locale: Locale?

    /**
     * The amount of chunks that the player will see in front of them,
     * excluding the chunk the player is in.
     */
    @get:JvmName("viewDistance")
    public val viewDistance: Int

    /**
     * The chat visibility of the player.
     */
    @get:JvmName("chatVisibility")
    public val chatVisibility: ChatVisibility

    /**
     * If the player accepts colours in chat messages.
     */
    @get:JvmName("hasChatColors")
    public val hasChatColors: Boolean

    /**
     * The skin parts that the player has shown.
     */
    @get:JvmName("skinParts")
    public val skinParts: SkinParts

    /**
     * The primary hand of the player.
     */
    @get:JvmName("mainHand")
    public val mainHand: MainHand

    /**
     * If the player allows appearing in the player sample on the server
     * status.
     */
    @get:JvmName("allowsServerListing")
    public val allowsServerListing: Boolean
}

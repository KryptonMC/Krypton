/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.resource

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.provide
import java.net.URI

/**
 * A resource pack that may be sent to clients.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ResourcePack {

    /**
     * The URI pointing to the location where this resource pack is from.
     */
    @get:JvmName("uri")
    public val uri: URI

    /**
     * The hash of the resource pack. This should be generated from hashing the
     * resource that the [uri] points to.
     */
    @get:JvmName("hash")
    public val hash: String

    /**
     * If clients must always display this resource pack.
     */
    public val isForced: Boolean

    /**
     * The message that will be shown to the client within the prompt used to
     * confirm the resource pack.
     */
    @get:JvmName("promptMessage")
    public val promptMessage: Component

    /**
     * Sends this resource pack to the given [player].
     *
     * @param player the player
     */
    public fun send(player: Player)

    @ApiStatus.Internal
    public interface Factory {

        public fun of(uri: URI, hash: String, isForced: Boolean, promptMessage: Component): ResourcePack
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new resource pack with the given values.
         *
         * @param uri the URI
         * @param hash the hash of the resource at the URI
         * @param isForced if the resource pack must be used by all clients
         * @param promptMessage the message sent with the prompt to accept the
         * pack
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(
            uri: URI,
            hash: String,
            isForced: Boolean,
            promptMessage: Component
        ): ResourcePack = FACTORY.of(uri, hash, isForced, promptMessage)
    }
}

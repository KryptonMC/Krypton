/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

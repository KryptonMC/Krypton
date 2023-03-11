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
package org.kryptonmc.krypton.locale

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.krypton.KryptonPlatform

object DisconnectMessages {

    /*
     * Outdated client/server for handshake.
     */

    @JvmField
    val OUTDATED_CLIENT: Component = translatable("multiplayer.disconnect.outdated_client", text(KryptonPlatform.minecraftVersion))
    @JvmField
    val INCOMPATIBLE: Component = translatable("multiplayer.disconnect.incompatible", text(KryptonPlatform.minecraftVersion))

    /*
     * Proxy handling in handshake.
     */

    @JvmField
    val LEGACY_FORWARDING_NOT_ENABLED: Component = text()
        .content("It appears that you have been forwarded using legacy forwarding by a proxy, but this server is not configured")
        .appendNewline()
        .append(text("to support legacy forwarding. Please contact a server administrator."))
        .build()
    @JvmField
    val TCPSHIELD_FORWARDING_NOT_ENABLED: Component = text()
        .content("It appears that you have been forwarded from TCPShield, but this server is not configured to support TCPShield forwarding.")
        .appendNewline()
        .append(text("Please contact a server administrator."))
        .build()
    @JvmField
    val FAILED_LEGACY_DECODE: Component = text("Failed to decode legacy data! Please report this to an administrator!")
    @JvmField
    val FAILED_TCPSHIELD_DECODE: Component = text("Failed to decode TCPShield data! Please report this to an administrator!")
    @JvmField
    val NO_DIRECT_CONNECT: Component = text("This server cannot be direct connected to whilst it has forwarding enabled.")
    @JvmField
    val SERVER_FULL: Component = translatable("multiplayer.disconnect.server_full")

    /*
     * General disconnects.
     */

    @JvmField
    val NOT_WHITELISTED: Component = translatable("multiplayer.disconnect.not_whitelisted")
    @JvmField
    val KICKED: Component = translatable("multiplayer.disconnect.kicked")
    @JvmField
    val UNVERIFIED_USERNAME: Component = translatable("multiplayer.disconnect.unverified_username")
    @JvmField
    val UNEXPECTED_QUERY_RESPONSE: Component = translatable("multiplayer.disconnect.unexpected_query_response")
    @JvmField
    val ILLEGAL_CHARACTERS: Component = translatable("multiplayer.disconnect.illegal_characters")
    @JvmField
    val OUT_OF_ORDER_CHAT: Component = translatable("multiplayer.disconnect.out_of_order_chat")
    @JvmField
    val CHAT_VALIDATION_FAILED: Component = translatable("multiplayer.disconnect.chat_validation_failed")
    @JvmField
    val REQUIRED_TEXTURE_PROMPT: Component = translatable("multiplayer.requiredTexturePrompt.disconnect")
    @JvmField
    val SERVER_SHUTDOWN: Component = translatable("multiplayer.disconnect.server_shutdown")

    @JvmField
    val TIMEOUT: Component = translatable("disconnect.timeout")
    @JvmField
    val END_OF_STREAM: Component = translatable("disconnect.endOfStream")
}

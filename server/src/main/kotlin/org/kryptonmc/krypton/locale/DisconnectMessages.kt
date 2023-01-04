/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
    val REQUIRED_TEXTURE_PROMPT: Component = translatable("multiplayer.requiredTexturePrompt.disconnect")
    @JvmField
    val SERVER_SHUTDOWN: Component = translatable("multiplayer.disconnect.server_shutdown")

    @JvmField
    val TIMEOUT: Component = translatable("disconnect.timeout")
    @JvmField
    val END_OF_STREAM: Component = translatable("disconnect.endOfStream")
}

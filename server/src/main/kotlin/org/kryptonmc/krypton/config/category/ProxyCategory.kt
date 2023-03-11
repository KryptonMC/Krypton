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
package org.kryptonmc.krypton.config.category

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
@JvmRecord
data class ProxyCategory(
    @Comment(MODE_COMMENT)
    val mode: Mode = Mode.NONE,
    @Comment("The forwarding secret from Velocity. Only used in the MODERN forwarding protocol.")
    val secret: String = ""
) {

    /**
     * If [authenticatesUsers] is true, the proxy that forwarded the player
     * will have also forwarded the player's game profile, meaning it has
     * already authenticated the player with Mojang, and that we don't have to.
     */
    enum class Mode(val authenticatesUsers: Boolean) {

        NONE(false),
        LEGACY(true),
        MODERN(true),
        TCPSHIELD(false)
    }

    companion object {

        // Hopefully this will look better when trimIndent is a candidate for constant evaluation in the K2 compiler.
        private const val MODE_COMMENT = """
The method to use for forwarding a connecting user's information on
to Krypton from a proxy server. Supported values are:
  - NONE - Disable forwarding support completely
  - LEGACY - Use the BungeeCord/pre-1.13 method
  - MODERN - Use Velocity's modern forwarding protocol
  - TCPSHIELD - Use TCPShield's forwarding protocol
When any mode other than NONE or TCPSHIELD is used, the server will be forced
offline and will ONLY accept connections from proxies. No users will be able to
direct connect.
"""
    }
}

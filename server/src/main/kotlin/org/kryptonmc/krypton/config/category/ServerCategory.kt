/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.config.category

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class ServerCategory(
    @Comment("The IP used by players to connect. 0.0.0.0 means listen on all interfaces.")
    val ip: String = DEFAULT_IP,
    @Comment("The port used by players to connect.")
    val port: Int = DEFAULT_PORT,
    @Setting("online-mode")
    @Comment("Whether the server authenticates users with Mojang.")
    val onlineMode: Boolean = true,
    @Setting("compression-threshold")
    @Comment("The threshold at which packets larger will be compressed. Set to -1 to disable.")
    val compressionThreshold: Int = 256
) {

    companion object {

        const val DEFAULT_IP = "0.0.0.0"
        const val DEFAULT_PORT = 25565
    }
}

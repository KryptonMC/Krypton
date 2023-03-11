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
data class ServerCategory(
    @Comment("The IP used by players to connect. 0.0.0.0 means listen on all interfaces.")
    val ip: String = "0.0.0.0",
    @Comment("The port used by players to connect.")
    val port: Int = 25565,
    @Comment("Whether the server authenticates users with Mojang.")
    val onlineMode: Boolean = true,
    @Comment("The threshold at which packets larger will be compressed. Set to -1 to disable.")
    val compressionThreshold: Int = 256,
    @Comment("If console messages should be sent to admins with the permission krypton.broadcast_admins")
    val broadcastConsoleToAdmins: Boolean = true,
    @Comment("Settings for the server resource pack")
    val resourcePack: ResourcePackCategory = ResourcePackCategory()
)

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
package org.kryptonmc.krypton

import org.kryptonmc.krypton.pack.resources.CloseableResourceManager
import org.kryptonmc.krypton.registry.dynamic.LayeredRegistryAccess
import org.kryptonmc.krypton.registry.dynamic.RegistryLayer
import org.kryptonmc.krypton.server.ReloadableResources
import org.kryptonmc.krypton.world.data.WorldData

class WorldLoadingData(
    val resourceManager: CloseableResourceManager,
    val dataPackResources: ReloadableResources,
    val registries: LayeredRegistryAccess<RegistryLayer>,
    val worldData: WorldData
) : AutoCloseable {

    override fun close() {
        resourceManager.close()
    }
}

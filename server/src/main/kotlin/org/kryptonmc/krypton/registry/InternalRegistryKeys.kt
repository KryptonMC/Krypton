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
package org.kryptonmc.krypton.registry

import org.kryptonmc.api.registry.RegistryKeys
import org.kryptonmc.krypton.entity.attribute.Attribute
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.event.GameEvent
import org.kryptonmc.krypton.world.fluid.Fluid

object InternalRegistryKeys {

    val ATTRIBUTE = RegistryKeys.minecraft<Attribute>("attribute")
    val METADATA = RegistryKeys.krypton<MetadataKey<*>>("metadata")
    val DIMENSION = RegistryKeys.minecraft<KryptonWorld>("dimension")
    val GAME_EVENT = RegistryKeys.minecraft<GameEvent>("game_event")
    val FLUID = RegistryKeys.minecraft<Fluid>("fluid")
}

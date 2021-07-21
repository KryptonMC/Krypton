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
package org.kryptonmc.krypton.pack.repository

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.pack.KryptonPackResources
import org.kryptonmc.krypton.pack.PackMetadata
import org.kryptonmc.krypton.pack.PackResources

class KryptonRepositorySource : RepositorySource {

    private val pack = KryptonPackResources(METADATA, setOf("minecraft"))

    override fun loadPacks(constructor: (String, () -> PackResources, Pack.Position, Boolean, PackSource) -> Pack, action: (Pack) -> Unit) {
        Pack.of(ID, { pack }, Pack.Position.BOTTOM, false, PackSource.BUILT_IN, constructor)?.let(action)
    }

    companion object {

        const val ID = "vanilla"
        val METADATA = PackMetadata(Component.translatable("dataPack.vanilla.description"), ServerInfo.PACK_VERSION)
    }
}

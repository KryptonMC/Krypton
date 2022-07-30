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
package org.kryptonmc.krypton.pack.repository

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.pack.VanillaPackResources
import org.kryptonmc.krypton.pack.metadata.PackMetadata
import java.util.function.Consumer

class ServerRepositorySource : RepositorySource {

    private val vanillaPack = VanillaPackResources(BUILT_IN_METADATA, "minecraft")

    override fun loadPacks(factory: Pack.Constructor, consumer: Consumer<Pack>) {
        val pack = Pack.create(VANILLA_ID, false, { vanillaPack }, factory, Pack.Position.BOTTOM, PackSource.BUILT_IN)
        if (pack != null) consumer.accept(pack)
    }

    companion object {

        @JvmField
        val BUILT_IN_METADATA: PackMetadata = PackMetadata(Component.translatable("dataPack.vanilla.description"), KryptonPlatform.dataPackVersion)
        const val VANILLA_ID: String = "vanilla"
    }
}

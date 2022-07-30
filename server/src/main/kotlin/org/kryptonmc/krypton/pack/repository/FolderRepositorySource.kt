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

import org.kryptonmc.krypton.pack.FilePackResources
import org.kryptonmc.krypton.pack.FolderPackResources
import org.kryptonmc.krypton.pack.PackResources
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.Supplier

class FolderRepositorySource(private val folder: Path, private val packSource: PackSource) : RepositorySource {

    override fun loadPacks(factory: Pack.Constructor, consumer: Consumer<Pack>) {
        if (!Files.isDirectory(folder)) Files.createDirectories(folder)
        Files.list(folder).filter(RESOURCE_OR_DATA_PACK_FILTER).forEach {
            val pack = Pack.create("file/${it.fileName}", false, createSupplier(it), factory, Pack.Position.TOP, packSource)
            if (pack != null) consumer.accept(pack)
        }
    }

    private fun createSupplier(path: Path): Supplier<PackResources> {
        if (Files.isDirectory(path)) return Supplier { FolderPackResources(path) }
        return Supplier { FilePackResources(path) }
    }

    companion object {

        private val RESOURCE_OR_DATA_PACK_FILTER: Predicate<Path> = Predicate {
            val isResourcePack = Files.isRegularFile(it) && it.toString().endsWith(".zip")
            val isDataPack = Files.isDirectory(it) && Files.isRegularFile(it.resolve(PackResources.PACK_META))
            isResourcePack || isDataPack
        }
    }
}

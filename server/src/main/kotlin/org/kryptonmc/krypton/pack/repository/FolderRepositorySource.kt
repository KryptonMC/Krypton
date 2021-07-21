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

import org.kryptonmc.krypton.pack.FilePackResources
import org.kryptonmc.krypton.pack.FolderPackResources
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.util.createDirectories
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.NotDirectoryException
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

class FolderRepositorySource(
    private val folder: Path,
    private val source: PackSource
) : RepositorySource {

    override fun loadPacks(constructor: (String, () -> PackResources, Pack.Position, Boolean, PackSource) -> Pack, action: (Pack) -> Unit) {
        if (!folder.isDirectory()) folder.createDirectories()
        try {
            Sequence { Files.newDirectoryStream(folder, RESOURCE_PACK_FILTER).iterator() }
        } catch (exception: Exception) {
            if (exception !is IOException && exception !is NotDirectoryException) throw exception
            return
        }.forEach {
            val path = "file/$it"
            Pack.of(path, createSupplier(it), Pack.Position.TOP, false, source, constructor)?.let(action)
        }
    }

    private fun createSupplier(file: Path): () -> PackResources = { if (file.isDirectory()) FolderPackResources(file) else FilePackResources(File(file.toUri()), file) }

    companion object {

        private val RESOURCE_PACK_FILTER = DirectoryStream.Filter<Path> {
            (it.isRegularFile() && it.toString().endsWith(".zip")) || (it.isDirectory() && it.resolve(PackResources.PACK_META).isRegularFile())
        }
    }
}

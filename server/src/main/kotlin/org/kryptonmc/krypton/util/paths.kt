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
package org.kryptonmc.krypton.util

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.FileAttribute

fun Path.createTempFile(
    prefix: String? = null,
    suffix: String? = null,
    vararg attributes: FileAttribute<*>
): Path = Files.createTempFile(this, prefix, suffix, *attributes)

fun Path.tryCreateDirectories(): Path = catchAndReturnSelf { Files.createDirectories(this) }

fun Path.tryCreateDirectory(): Path = catchAndReturnSelf { Files.createDirectory(this) }

fun Path.tryCreateFile(): Path = catchAndReturnSelf { Files.createFile(this) }

fun Path.forEachDirectoryEntry(predicate: (Path) -> Boolean, action: (Path) -> Unit) {
    Files.newDirectoryStream(this, predicate).use { it.forEach(action) }
}

private fun Path.catchAndReturnSelf(action: () -> Path): Path = try {
    action()
} catch (exception: Exception) {
    this
}

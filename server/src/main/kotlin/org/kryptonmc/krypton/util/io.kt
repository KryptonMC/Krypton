/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util

import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.FileChannel
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.attribute.FileAttribute

fun Path.openChannel(vararg options: OpenOption): FileChannel = FileChannel.open(this, *options)

fun Path.openChannel(options: Set<OpenOption>): FileChannel = FileChannel.open(this, options)

fun Path.createTempFile(
    prefix: String? = null,
    suffix: String? = null,
    vararg attributes: FileAttribute<*>
): Path = Files.createTempFile(this, prefix, suffix, *attributes)

fun Path.createDirectories(): Path = catchAndReturnSelf { Files.createDirectories(this) }

fun Path.createDirectory(): Path = catchAndReturnSelf { Files.createDirectory(this) }

fun Path.createFile(): Path = catchAndReturnSelf { Files.createFile(this) }

fun Path.list(): Sequence<Path> = Sequence { Files.list(this).iterator() }

fun InputStream.copyTo(path: Path) = Files.copy(this, path)

private fun Path.catchAndReturnSelf(action: () -> Path): Path = try {
    action()
} catch (exception: Exception) {
    this
}

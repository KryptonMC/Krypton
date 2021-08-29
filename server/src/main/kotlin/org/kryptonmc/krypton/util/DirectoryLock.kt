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

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.isDirectory

class DirectoryLock private constructor(
    private val channel: FileChannel,
    private val lock: FileLock
) : AutoCloseable {

    val isValid: Boolean
        get() = lock.isValid

    override fun close() {
        try {
            if (lock.isValid) lock.release()
        } finally {
            if (channel.isOpen) channel.close()
        }
    }

    private class LockException(path: Path, reason: String) : IOException("${path.toAbsolutePath()} $reason") {

        constructor(path: Path) : this(path, "already locked!")
    }

    companion object {

        private const val FILE_NAME = "session.lock"
        private val CONTENT = kotlin.run {
            val bytes = "☃".encodeToByteArray()
            ByteBuffer.allocateDirect(bytes.size).put(bytes).flip()
        }

        fun create(folder: Path): DirectoryLock {
            val lockFile = folder.resolve(FILE_NAME)
            if (!folder.isDirectory()) folder.tryCreateDirectories()

            val channel = FileChannel.open(lockFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE)
            channel.write(CONTENT.duplicate())
            channel.force(true)

            val lock = channel.tryLock() ?: throw LockException(lockFile)
            return DirectoryLock(channel, lock)
        }
    }
}

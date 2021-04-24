package org.kryptonmc.krypton.util

import java.io.File
import java.io.InputStream
import java.nio.channels.FileChannel
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.attribute.FileAttribute

fun Path.openChannel(vararg options: OpenOption): FileChannel = FileChannel.open(this, *options)

fun Path.openChannel(options: Set<OpenOption>): FileChannel = FileChannel.open(this, options)

fun Path.createTempFile(prefix: String? = null, suffix: String? = null, vararg attributes: FileAttribute<*>): Path =
    Files.createTempFile(this, prefix, suffix, *attributes)

fun Path.newInputStream(): InputStream = Files.newInputStream(this)

fun Path.deleteIfExists() = Files.deleteIfExists(this)

fun Path.moveTo(target: Path, vararg options: CopyOption): Path = Files.move(this, target, *options)

val Path.isRegularFile: Boolean
    get() = Files.isRegularFile(this)

val Path.isDirectory: Boolean
    get() = Files.isDirectory(this)

val Path.size: Long
    get() = Files.size(this)

fun InputStream.copyTo(file: File) = Files.copy(this, file.toPath())

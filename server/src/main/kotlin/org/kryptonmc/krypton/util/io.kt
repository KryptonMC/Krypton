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

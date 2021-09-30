package org.kryptonmc.generators

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

fun Path.tryCreateDirectories() = catchAndReturnSelf { createDirectories() }

fun Path.tryCreateFile() = catchAndReturnSelf { createFile() }

private fun <T> T.catchAndReturnSelf(action: () -> Unit): T = try {
    action()
    this
} catch (exception: Exception) {
    this
}

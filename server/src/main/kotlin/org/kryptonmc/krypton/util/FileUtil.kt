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
package org.kryptonmc.krypton.util

import org.kryptonmc.serialization.DataResult
import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern

object FileUtil {

    private val STRICT_PATH_SEGMENT_CHECK = Pattern.compile("[-._a-z0-9]+")

    @JvmStatic
    fun decomposePath(path: String): DataResult<List<String>> {
        var slash = path.indexOf('/')
        if (slash == -1) {
            return when (path) {
                "", ".", ".." -> DataResult.error("Invalid path $path!")
                else -> if (!isValidStrictPathSegment(path)) DataResult.error("Invalid path $path!") else DataResult.success(ImmutableLists.of(path))
            }
        }

        val result = ArrayList<String>()
        var cursor = 0
        var finished = false

        while (true) {
            val segment = path.substring(cursor, slash)
            if (segment == "" || segment == "." || segment == "..") return DataResult.error("Invalid segment $segment in path $path!")
            if (!isValidStrictPathSegment(segment)) return DataResult.error("Invalid segment $segment in path $path!")

            result.add(segment)
            if (finished) return DataResult.success(result)
            cursor = slash + 1
            slash = path.indexOf('/', cursor)

            if (slash == -1) {
                slash = path.length
                finished = true
            }
        }
    }

    @JvmStatic
    fun resolvePath(root: Path, segments: List<String>): Path = when (val size = segments.size) {
        0 -> root
        1 -> root.resolve(segments.get(0))
        else -> {
            val path = arrayOfNulls<String>(size - 1)
            for (i in 1 until size) {
                path[i - 1] = segments.get(i)
            }
            root.resolve(NoSpread.fileSystemGetPath(root.fileSystem, segments.get(0), path))
        }
    }

    @JvmStatic
    fun isValidStrictPathSegment(segment: String): Boolean = STRICT_PATH_SEGMENT_CHECK.matcher(segment).matches()

    @JvmStatic
    fun validatePath(path: Array<out String>) {
        require(path.isNotEmpty()) { "Path must have at least one element!" }
        path.forEach {
            require(it != ".." && it != "." && isValidStrictPathSegment(it)) { "Illegal segment $it in path ${path.contentToString()}!" }
        }
    }

    @JvmStatic
    fun createDirectoriesSafe(path: Path) {
        val safePath = if (Files.exists(path)) path.toRealPath() else path
        Files.createDirectories(safePath)
    }
}

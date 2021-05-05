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
package org.kryptonmc.krypton.plugin

import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Path

/**
 * The class loader used to load plugins. This is only so we can expose addURL for internal use
 */
class PluginClassLoader(vararg urls: URL) : URLClassLoader(urls) {

    constructor(path: Path) : this(path.toUri().toURL())

    internal fun addPath(path: Path) {
        addURL(path.toUri().toURL())
    }

    companion object {

        init {
            ClassLoader.registerAsParallelCapable()
        }
    }
}

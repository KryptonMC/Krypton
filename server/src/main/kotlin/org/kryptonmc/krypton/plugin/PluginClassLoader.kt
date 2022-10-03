/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2018 Velocity Contributors
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
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/PaperMC/Velocity/blob/0097359a99c23de4fc6b92c59a401a10208b4c4a/proxy/src/main/java/com/velocitypowered/proxy/plugin/PluginClassLoader.java
 */
package org.kryptonmc.krypton.plugin

import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Path
import java.util.concurrent.CopyOnWriteArraySet

/**
 * The class loader used to load plugins. This is to allow shared resources across
 * multiple loaded plugins.
 */
class PluginClassLoader(vararg urls: URL) : URLClassLoader(urls) {

    constructor(path: Path) : this(path.toUri().toURL())

    fun addToLoaders(): PluginClassLoader = apply { loaders.add(this) }

    fun addPath(path: Path) {
        addURL(path.toUri().toURL())
    }

    override fun close() {
        loaders.remove(this)
        super.close()
    }

    override fun loadClass(name: String, resolve: Boolean): Class<*> = loadClass0(name, resolve, true)

    private fun loadClass0(name: String, resolve: Boolean, checkOther: Boolean): Class<*> {
        try {
            return super.loadClass(name, resolve)
        } catch (_: ClassNotFoundException) {
            // ignored - we'll try others
        }

        if (!checkOther) throw ClassNotFoundException(name)
        loaders.forEach {
            if (it == this) return@forEach
            try {
                return it.loadClass0(name, resolve, false)
            } catch (_: ClassNotFoundException) {
                // oh well, we'll try everyone else first
            }
        }
        throw ClassNotFoundException(name)
    }

    companion object {

        private val loaders = CopyOnWriteArraySet<PluginClassLoader>()

        init {
            registerAsParallelCapable()
        }
    }
}

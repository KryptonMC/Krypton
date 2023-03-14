/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the Apache License v2.0
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/PaperMC/Velocity/blob/959e75d16db352924e679fb5be545ee9b264fbd2/proxy/src/main/java/com/velocitypowered/proxy/plugin/PluginClassLoader.java
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

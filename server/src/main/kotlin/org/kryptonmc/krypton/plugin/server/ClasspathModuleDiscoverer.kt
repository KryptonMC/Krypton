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
package org.kryptonmc.krypton.plugin.server

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import java.nio.file.Path

class ClasspathModuleDiscoverer(private val reflections: Reflections, private val resourceLoader: ClassLoader) : ModuleDiscoverer {

    override fun discover(): Collection<Path> {
        val files = reflections.getResources("(.*).json")
        val result = ArrayList<Path>()
        files.forEach {
            val url = resourceLoader.getResource(it) ?: error("Failed to resolve resource $it found by reflections! This is a bug!")
            val path = Path.of(url.toURI())
            result.add(path)
        }
        return result
    }

    companion object {

        @JvmStatic
        fun createDefault(): ClasspathModuleDiscoverer {
            val config = ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(Scanners.Resources)
                .filterInputsBy { it.contains("krypton-modules") }
            val reflections = Reflections(config)
            val resourceLoader = Thread.currentThread().contextClassLoader
            return ClasspathModuleDiscoverer(reflections, resourceLoader)
        }
    }
}

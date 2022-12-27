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
package org.kryptonmc.krypton.pack.repository

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.pack.PackType
import org.kryptonmc.krypton.pack.VanillaPackResources
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function

abstract class BuiltInPackSource(
    private val packType: PackType,
    val vanillaPack: VanillaPackResources,
    private val packDirectory: Key
) : RepositorySource {

    override fun loadPacks(action: Consumer<Pack>) {
        createVanillaPack(vanillaPack)?.let(action::accept)
        listBundledPacks(action)
    }

    protected abstract fun createVanillaPack(resources: PackResources): Pack?

    protected abstract fun getPackTitle(pack: String): Component

    private fun listBundledPacks(consumer: Consumer<Pack>) {
        val results = HashMap<String, Function<String, Pack?>>()
        populatePackList { key, function -> results.put(key, function) }
        results.forEach { (key, function) ->
            val pack = function.apply(key)
            if (pack != null) consumer.accept(pack)
        }
    }

    protected fun populatePackList(action: BiConsumer<String, Function<String, Pack?>>) {
        vanillaPack.listRawPaths(packType, packDirectory) { discoverPacksInPath(it, action) }
    }

    protected fun discoverPacksInPath(path: Path?, action: BiConsumer<String, Function<String, Pack?>>) {
        if (path == null || !Files.isDirectory(path)) return
        try {
            FolderRepositorySource.discoverPacks(path, true) { relativePath, resources ->
                action.accept(pathToId(relativePath)) { createBuiltinPack(it, resources, getPackTitle(it)) }
            }
        } catch (exception: IOException) {
            LOGGER.warn("Failed to discover packs in $path.", exception)
        }
    }

    protected abstract fun createBuiltinPack(id: String, resources: Pack.ResourcesSupplier, title: Component): Pack?

    companion object {

        private val LOGGER = LogManager.getLogger()
        const val VANILLA_ID: String = "vanilla"

        @JvmStatic
        private fun pathToId(path: Path): String = StringUtils.removeEnd(path.fileName.toString(), ".zip")
    }
}

/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
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

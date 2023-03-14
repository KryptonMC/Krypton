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
package org.kryptonmc.krypton.plugin.loader

import com.google.common.collect.ImmutableList
import com.google.gson.stream.JsonReader
import com.google.inject.Module
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.plugin.InvalidPluginException
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDependency
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.krypton.plugin.KryptonPluginContainer
import org.kryptonmc.krypton.plugin.KryptonPluginDependency
import org.kryptonmc.krypton.plugin.KryptonPluginDescription
import org.kryptonmc.krypton.plugin.PluginClassLoader
import org.kryptonmc.krypton.plugin.module.PluginModule
import org.kryptonmc.krypton.util.NoSpread
import org.kryptonmc.processor.SerializedPluginDescription
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.JarInputStream

/**
 * Various utilities for loading and creating plugins.
 */
class JarPluginSource(private val pluginsDirectory: Path) : PluginSource {

    override fun loadDescriptions(): Collection<PluginDescription> {
        val result = ArrayList<PluginDescription>()
        Files.newDirectoryStream(pluginsDirectory) { Files.isRegularFile(it) && it.toString().endsWith(".jar") }.use { stream ->
            stream.forEach { path ->
                try {
                    val description = loadDescription(path)
                    result.add(description)
                } catch (exception: Exception) {
                    LOGGER.error("Failed to load plugin at $path!", exception)
                }
            }
        }
        return result
    }

    private fun loadDescription(source: Path): LoadedCandidateDescription {
        val serialized = findMetadata(source) ?: throw InvalidPluginException("Could not find a valid krypton-plugin-meta.json file!")
        if (!serialized.id.matches(SerializedPluginDescription.ID_REGEX)) throw InvalidPluginException("Plugin ID ${serialized.id} is invalid!")
        return serializedToCandidate(serialized, source)
    }

    private fun findMetadata(path: Path): SerializedPluginDescription? = JarInputStream(Files.newInputStream(path)).use { input ->
        var entry = input.nextJarEntry
        while (entry != null) {
            if (entry.name == "krypton-plugin-meta.json") return JsonReader(InputStreamReader(input)).use(SerializedPluginDescription::read)
            entry = input.nextJarEntry
        }
        return null
    }

    override fun loadPlugin(candidate: PluginDescription): PluginDescription {
        require(candidate is LoadedCandidateDescription) { "Candidate description provided isn't compatible with this loader!" }
        val loader = PluginClassLoader(candidate.source!!).addToLoaders()
        val mainClass = loader.loadClass(candidate.mainClass)
        return candidate.toFull(mainClass)
    }

    override fun createPluginContainer(description: PluginDescription): PluginContainer = KryptonPluginContainer(description, false)

    override fun createModule(container: PluginContainer): Module {
        val description = container.description
        require(description is LoadedDescription) { "Description for provided container isn't compatible with this loader!" }
        return PluginModule(container, description.mainClass, pluginsDirectory)
    }

    override fun createPlugin(container: PluginContainer, vararg modules: Module) {
        require(container is KryptonPluginContainer) { "Container provided isn't compatible with this loader!" }
        val description = container.description
        require(description is LoadedDescription) { "Description provided isn't compatible with this loader!" }

        val injector = NoSpread.guiceCreateInjector(modules)
        val instance = requireNotNull(injector.getInstance(description.mainClass)) { "Got nothing from injector for plugin ${description.id}!" }

        container.instance = instance
    }

    override fun onPluginLoaded(container: PluginContainer) {
        val description = container.description
        LOGGER.info("Successfully loaded plugin ${description.id} version ${description.version} by ${description.authors.joinToString()}")
    }

    private class LoadedCandidateDescription(
        from: SerializedPluginDescription,
        source: Path,
        val mainClass: String
    ) : KryptonPluginDescription(from.id, from.name, from.version, from.description, convertAuthors(from), convertDependencies(from), source) {

        fun toFull(mainClass: Class<*>): LoadedDescription = LoadedDescription(this, mainClass)

        companion object {

            @JvmStatic
            private fun convertAuthors(from: SerializedPluginDescription): List<String> = ImmutableList.copyOf(from.authors)

            @JvmStatic
            private fun convertDependencies(from: SerializedPluginDescription): Collection<PluginDependency> {
                val result = ImmutableList.builder<PluginDependency>()
                from.dependencies.forEach { result.add(KryptonPluginDependency(it.id, it.optional)) }
                return result.build()
            }
        }
    }

    private class LoadedDescription(
        from: LoadedCandidateDescription,
        val mainClass: Class<*>
    ) : KryptonPluginDescription(from.id, from.name, from.version, from.description, from.authors, from.dependencies, from.source)

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun serializedToCandidate(description: SerializedPluginDescription, source: Path): LoadedCandidateDescription =
            LoadedCandidateDescription(description, source, description.main)
    }
}

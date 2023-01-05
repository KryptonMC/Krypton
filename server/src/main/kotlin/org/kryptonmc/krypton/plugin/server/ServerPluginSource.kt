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

import com.google.common.collect.ImmutableList
import com.google.gson.stream.JsonReader
import com.google.inject.Module
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDependency
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.krypton.plugin.KryptonPluginContainer
import org.kryptonmc.krypton.plugin.KryptonPluginDependency
import org.kryptonmc.krypton.plugin.KryptonPluginDescription
import org.kryptonmc.krypton.plugin.loader.PluginSource
import org.kryptonmc.krypton.plugin.module.PluginModule
import org.kryptonmc.krypton.util.NoSpread
import org.kryptonmc.processor.SerializedPluginDescription
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path

class ServerPluginSource(private val modulesDirectory: Path, private val modules: ServerModules) : PluginSource {

    override fun loadDescriptions(): Collection<PluginDescription> {
        val classpath = getClasspathAsPath()
        val result = ArrayList<PluginDescription>()
        Files.newDirectoryStream(classpath) { Files.isRegularFile(it) && it.toString().endsWith(MODULE_FILE_SUFFIX) }.use { stream ->
            stream.forEach { path ->
                try {
                    val description = loadDescription(path)
                    if (!modules.isEnabled(description.id)) return@forEach // Don't load disabled modules
                    result.add(loadDescription(path))
                } catch (exception: Exception) {
                    LOGGER.error("Failed to load internal module at $path!", exception)
                }
            }
        }
        if (result.isEmpty()) LOGGER.warn("No internal modules found on classpath.")
        return result
    }

    private fun loadDescription(source: Path): LoadedCandidateDescription {
        val serialized = findMetadata(source) ?: throw InvalidModuleException("Module ${getModuleNameFromPath(source)} has invalid metadata!")
        if (!serialized.id.matches(SerializedPluginDescription.ID_REGEX)) throw InvalidModuleException("Module ID ${serialized.id} is invalid!")
        if (!serialized.id.endsWith("-module")) {
            throw InvalidModuleException("Internal modules must have an ID ending with '-module', but ${serialized.id} does not!")
        }
        return serializedToCandidate(serialized)
    }

    private fun findMetadata(path: Path): SerializedPluginDescription? {
        val reader = JsonReader(InputStreamReader(Files.newInputStream(path)))
        return SerializedPluginDescription.read(reader)
    }

    override fun loadPlugin(candidate: PluginDescription): PluginDescription {
        require(candidate is LoadedCandidateDescription) { "Description provided isn't a loaded module candidate!" }
        val mainClass = Thread.currentThread().contextClassLoader.loadClass(candidate.mainClass)
        return candidate.toFull(mainClass)
    }

    override fun createModule(container: PluginContainer): Module {
        val description = container.description
        require(description is LoadedDescription) { "Description for provided container isn't compatible with this loader!" }
        return PluginModule(container, description.mainClass, modulesDirectory)
    }

    override fun createPlugin(container: PluginContainer, vararg modules: Module) {
        require(container is KryptonPluginContainer) { "Container provided isn't compatible with this loader!" }
        val description = container.description
        require(description is LoadedDescription) { "Description provided isn't compatible with this loader!" }

        val injector = NoSpread.guiceCreateInjector(modules)
        val instance = requireNotNull(injector.getInstance(description.mainClass)) { "Got nothing from injector for module ${description.id}!" }

        container.instance = instance
    }

    override fun onPluginsLoaded(containers: Collection<PluginContainer>) {
        val result = StringBuilder()
        var i = 0
        modules.all.forEach { name ->
            result.append('\t')
            result.append("Module $name: ")
            if (modules.isEnabled(name)) {
                if (containers.any { it.description.id.startsWith(name) }) {
                    result.append("Loaded")
                } else {
                    result.append("Enabled not loaded, check for errors")
                }
            } else {
                result.append("Disabled")
            }
            if (i != modules.all.size - 1) result.append('\n')
            ++i
        }
        LOGGER.info("Server module status: \n$result")
    }

    private class LoadedCandidateDescription(
        from: SerializedPluginDescription,
        val mainClass: String
    ) : KryptonPluginDescription(from.id, from.name, from.version, from.description, convertAuthors(from), convertDependencies(from), null) {

        fun toFull(mainClass: Class<*>): LoadedDescription = LoadedDescription(this, mainClass)

        companion object {

            @JvmStatic
            private fun convertAuthors(from: SerializedPluginDescription): List<String> = ImmutableList.copyOf(from.authors)

            @JvmStatic
            fun convertDependencies(from: SerializedPluginDescription): Collection<PluginDependency> {
                val result = ImmutableList.builder<PluginDependency>()
                from.dependencies.forEach { result.add(KryptonPluginDependency(it.id, it.optional)) }
                return result.build()
            }
        }
    }

    private class LoadedDescription(
        from: LoadedCandidateDescription,
        val mainClass: Class<*>
    ) : KryptonPluginDescription(from.id, from.name, from.version, from.description, from.authors, from.dependencies, null)

    companion object {

        private const val MODULE_FILE_SUFFIX = ".krypton-module.json"
        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun getModuleNameFromPath(path: Path): String {
            val pathName = path.toString()
            val lengthOfModuleName = pathName.length - MODULE_FILE_SUFFIX.length
            return pathName.substring(0, lengthOfModuleName)
        }

        @JvmStatic
        private fun getClasspathAsPath(): Path = Path.of(ServerPluginSource::class.java.protectionDomain.codeSource.location.toURI())

        @JvmStatic
        private fun serializedToCandidate(description: SerializedPluginDescription): LoadedCandidateDescription =
            LoadedCandidateDescription(description, description.main)
    }
}

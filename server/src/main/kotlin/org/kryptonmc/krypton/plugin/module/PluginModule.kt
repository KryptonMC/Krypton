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
package org.kryptonmc.krypton.plugin.module

import com.google.inject.Scopes
import dev.misfitlabs.kotlinguice4.KotlinModule
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.event.Event
import org.kryptonmc.api.event.EventNode
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.api.plugin.annotation.DataFolder
import org.kryptonmc.krypton.event.KryptonGlobalEventNode
import java.nio.file.Path

class PluginModule(
    private val container: PluginContainer,
    private val mainClass: Class<*>,
    private val basePath: Path
) : KotlinModule() {

    override fun configure() {
        bind(mainClass).`in`(Scopes.SINGLETON)
        bind<Logger>().toInstance(LogManager.getLogger(container.description.id))
        bind<Path>().annotatedWith<DataFolder>().toInstance(basePath.resolve(container.description.id))
        bind<PluginDescription>().toInstance(container.description)
        bind<PluginContainer>().toInstance(container)
        bindEventNode()
    }

    private fun bindEventNode() {
        val node = EventNode.all(container.description.id)
        bind<EventNode<Event>>().toInstance(node)
        PLUGINS_EVENT_NODE.addChild(node)
    }

    companion object {

        private val PLUGINS_EVENT_NODE = createPluginsEventNode()

        @JvmStatic
        private fun createPluginsEventNode(): EventNode<Event> {
            val node = EventNode.all("plugins")
            KryptonGlobalEventNode.addChild(node)
            return node
        }
    }
}

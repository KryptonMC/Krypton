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

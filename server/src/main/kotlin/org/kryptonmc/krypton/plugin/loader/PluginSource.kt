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

import com.google.inject.Module
import org.kryptonmc.api.event.Event
import org.kryptonmc.api.event.EventNode
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDescription

interface PluginSource {

    fun loadDescriptions(): Collection<PluginDescription>

    fun loadPlugin(candidate: PluginDescription): PluginDescription

    fun createPluginContainer(description: PluginDescription): PluginContainer

    fun createModule(container: PluginContainer): Module

    fun createPlugin(container: PluginContainer, vararg modules: Module)

    fun createEventNode(container: PluginContainer): EventNode<Event> {
        return EventNode.all(container.description.id)
    }

    fun onPluginLoaded(container: PluginContainer) {
        // Do nothing by default
    }

    fun onPluginsLoaded(containers: Collection<PluginContainer>) {
        // Do nothing by default
    }
}

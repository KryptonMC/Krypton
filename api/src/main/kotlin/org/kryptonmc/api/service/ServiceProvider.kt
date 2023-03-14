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
package org.kryptonmc.api.service

import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Represents a provider of a service of type [T].
 *
 * Plugins can use these to provide classes to other plugins in a way that
 * allows them to not need to know who they are actually providing the service
 * to (if anyone), which is a neat abstraction layer.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface ServiceProvider<T> {

    /**
     * The plugin that provided this service.
     */
    @get:JvmName("plugin")
    public val plugin: PluginContainer

    /**
     * The class of the service being provided.
     */
    @get:JvmName("type")
    public val type: Class<T>

    /**
     * The service provided.
     */
    @get:JvmName("service")
    public val service: T
}

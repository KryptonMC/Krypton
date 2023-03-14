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
 * For the original file that this file is derived from, see:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/PluginDescription.java
 */
package org.kryptonmc.api.plugin

import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Information for a plugin's dependency on another plugin.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface PluginDependency {

    /**
     * The plugin ID of the **dependency**, not the **dependent**.
     */
    @get:JvmName("id")
    public val id: String

    /**
     * If the dependency is optional or not.
     */
    public val isOptional: Boolean
}

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
 * For the original file that this file is derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/PluginContainer.java
 */
package org.kryptonmc.api.plugin

/**
 * A wrapper around a loaded plugin, that may be injected to gain access to
 * some details that may otherwise be unavailable.
 */
public interface PluginContainer {

    /**
     * The description of this plugin. This contains information that describes
     * this plugin.
     *
     * For more information, see [PluginDescription].
     */
    public val description: PluginDescription

    /**
     * The instance of the main class that has been created and injected in to.
     *
     * This may be null in the case that this is injected in to the constructor
     * of your main class, due to the instance not being available until that
     * injection taking place.
     * This option here avoids the chicken and egg problem that would otherwise
     * ensue.
     */
    public val instance: Any?
}

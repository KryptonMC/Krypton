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
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/Plugin.java
 */
package org.kryptonmc.api.plugin.annotation

import org.intellij.lang.annotations.Pattern

/**
 * A metadata annotation used to describe a plugin.
 *
 * This has to go on the plugin's main class, and it will be the injection
 * point for the plugin. This is similar in functionality to the standard JVM
 * entry point of the [`main` method](https://docs.oracle.com/javase/tutorial/getStarted/application/index.html).
 *
 * @param id the identifier for this plugin
 * @param name the human readable name of this plugin
 * @param version the version of this plugin, or empty for undefined
 * @param description the description of this plugin, explaining what it can be
 * used for
 * @param authors a list of people who helped create this plugin
 * @param dependencies a list of other plugins that this plugin depends on
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
public annotation class Plugin(
    @Pattern(ID_REGEX) public val id: String,
    public val name: String = "",
    public val version: String = "",
    public val description: String = "",
    public val authors: Array<String> = [],
    public val dependencies: Array<Dependency> = []
) {

    public companion object {

        /**
         * A regex for validating that your plugin ID is fine.
         *
         * If you want to check this on the web, it is recommended to copy this
         * in to [regex101](https://regex101.com), and then put in your plugin
         * ID and see if it matches.
         */
        public const val ID_REGEX: String = "[a-z][a-z0-9-_]{0,63}"
    }
}

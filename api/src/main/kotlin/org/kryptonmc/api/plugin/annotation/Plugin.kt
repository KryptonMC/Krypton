/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
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

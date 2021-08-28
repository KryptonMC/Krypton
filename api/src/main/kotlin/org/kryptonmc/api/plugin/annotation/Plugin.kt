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
 * Annotation used to describe a plugin.
 *
 * @param id the identifier for this plugin. This ID **should be unique**, to avoid conflicts
 * with other plugins. It is recommended, though not required however, to make this
 * something that other plugins could easily be able to use and recognise, as they
 * will use this to depend on your plugin. In addition, this may contain alphanumeric
 * characters, dashes, and underscores, and it must be between 1 and 64 characters in
 * length
 * @param name the human readable name of this plugin, as to be used in descriptions and
 * similar tags. This does not have to be unique, but is recommended to be, to
 * avoid confusion for users with other plugins with the same name
 * @param version the version of this plugin, or empty for undefined
 * @param description the description of this plugin, explaining what it can be used for
 * @param authors a list of people who helped create this plugin
 * @param dependencies a list of other plugins that this plugin depends on
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Plugin(
    @Pattern("[A-Za-z0-9-_]+") val id: String,
    val name: String = "",
    val version: String = "",
    val description: String = "",
    val authors: Array<String> = [],
    val dependencies: Array<Dependency> = []
)

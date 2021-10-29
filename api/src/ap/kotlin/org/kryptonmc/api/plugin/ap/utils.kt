/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.plugin.ap

import org.kryptonmc.api.plugin.annotation.Plugin

fun Plugin.toDescription(qualifiedName: String) = SerializedPluginDescription(
    id,
    name,
    version,
    description,
    authors.toList(),
    dependencies.map { SerializedDependency(it.id, it.optional) },
    qualifiedName
)

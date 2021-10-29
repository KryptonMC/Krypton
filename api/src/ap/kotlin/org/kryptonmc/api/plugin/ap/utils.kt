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

package org.kryptonmc.api.plugin.ap

import org.kryptonmc.api.plugin.annotation.Plugin

class SerializedPluginDescription(
    val id: String,
    val name: String?,
    val version: String?,
    val description: String?,
    val authors: List<String>?,
    val dependencies: List<SerializedDependency>?,
    val main: String
) {

    init {
        require(id matches ID_REGEX) { "ID is invalid! Should match $ID_REGEX, was $id" }
    }

    companion object {

        val ID_REGEX = "[a-z][a-z0-9-_]{0,63}".toRegex()
    }
}

data class SerializedDependency(val id: String, val optional: Boolean)

fun Plugin.toDescription(qualifiedName: String) = SerializedPluginDescription(
    id,
    name,
    version,
    description,
    authors.toList(),
    dependencies.map { SerializedDependency(it.id, it.optional) },
    qualifiedName
)

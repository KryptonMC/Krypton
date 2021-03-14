package org.kryptonmc.krypton.registry.tags

import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStreamReader
import java.net.URI
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class TagManager {

    val blockTags: List<Tag>
    val entityTypeTags: List<Tag>
    val fluidTags: List<Tag>
    val itemTags: List<Tag>

    private val allTags: List<Tag>
        get() = blockTags + entityTypeTags + fluidTags + itemTags

    init {
        val jarFile = File(javaClass.protectionDomain.codeSource.location.toURI())

        var fileSystem: FileSystem? = null
        val rootPath = if (jarFile.isFile) {
            fileSystem = FileSystems.newFileSystem(URI.create("jar:${jarFile.toURI()}"), mapOf<String, Any>())
            fileSystem.getPath("/registries")
        } else {
            Paths.get(URI.create("file://${javaClass.classLoader.getResource("registries")!!.file}"))
        }

        val entries = Files.walk(rootPath, Int.MAX_VALUE).filter { !Files.isDirectory(it) && it.toString().endsWith(".json") }.collect(Collectors.toList())

        fun getTagsAtPath(path: String): List<Tag> {
            return entries.filter { it.parent.toUri().toString().removeSuffix("/").endsWith(path) }.map {
                val tagName = it.fileName.toString().removeSuffix(".json")
                Tag(tagName, JSON.decodeFromString(TagData.serializer(), InputStreamReader(Files.newInputStream(it)).readText()))
            }.toList()
        }

        blockTags = getTagsAtPath("registries/tags/blocks")
        entityTypeTags = getTagsAtPath("registries/tags/entity_types")
        fluidTags = getTagsAtPath("registries/tags/fluids")
        itemTags = getTagsAtPath("registries/tags/items")

        fileSystem?.close()
    }

    fun getDeepTags(tag: Tag): List<Tag> {
        val tags = mutableListOf(tag)
        for (value in tag.data.values.filter { it.startsWith('#') }) {
            tags += getDeepTags(allTags.single { it.name == value.substring(NAMESPACE_CHARACTERS) })
        }
        return tags
    }

    companion object {

        private val JSON = Json {}

        // the amount of characters to strip the string "#minecraft:" from the front of a string
        private const val NAMESPACE_CHARACTERS = 11
    }
}

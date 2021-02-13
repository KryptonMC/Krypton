package me.bristermitten.minekraft.registry.tags

import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStreamReader
import java.util.jar.JarFile

class TagManager {

    val blockTags: List<Tag>
    val entityTypeTags: List<Tag>
    val fluidTags: List<Tag>
    val itemTags: List<Tag>

    private val allTags: List<Tag>
        get() = blockTags + entityTypeTags + fluidTags + itemTags

    init {
        val jarFile = JarFile(File(javaClass.protectionDomain.codeSource.location.toURI()))
        val jarEntries = jarFile.entries().asSequence().filter { !it.isDirectory && it.name.endsWith(".json") }.toList()

        val blockTagFiles = jarEntries.filter { it.name.startsWith("tags/blocks/") }
        val entityTypeTagFiles = jarEntries.filter { it.name.startsWith("tags/entity_types/") }
        val fluidTagFiles = jarEntries.filter { it.name.startsWith("tags/fluids/") }
        val itemTagFiles = jarEntries.filter { it.name.startsWith("tags/items/") }

        val blockTags = mutableListOf<Tag>()
        val entityTypeTags = mutableListOf<Tag>()
        val fluidTags = mutableListOf<Tag>()
        val itemTags = mutableListOf<Tag>()

        blockTagFiles.forEach {
            val tagName = it.name.removePrefix("tags/blocks/").removeSuffix(".json")
            blockTags += Tag(tagName, JSON.decodeFromString(TagData.serializer(), InputStreamReader(javaClass.classLoader.getResourceAsStream(it.name)!!).readText()))
        }
        entityTypeTagFiles.forEach {
            val tagName = it.name.removePrefix("tags/entity_types/").removeSuffix(".json")
            entityTypeTags += Tag(tagName, JSON.decodeFromString(TagData.serializer(), InputStreamReader(javaClass.classLoader.getResourceAsStream(it.name)!!).readText()))
        }
        fluidTagFiles.forEach {
            val tagName = it.name.removePrefix("tags/fluids/").removeSuffix(".json")
            fluidTags += Tag(tagName, JSON.decodeFromString(TagData.serializer(), InputStreamReader(javaClass.classLoader.getResourceAsStream(it.name)!!).readText()))
        }
        itemTagFiles.forEach {
            val tagName = it.name.removePrefix("tags/items/").removeSuffix(".json")
            itemTags += Tag(tagName, JSON.decodeFromString(TagData.serializer(), InputStreamReader(javaClass.classLoader.getResourceAsStream(it.name)!!).readText()))
        }

        this.blockTags = blockTags.toList()
        this.entityTypeTags = entityTypeTags.toList()
        this.fluidTags = fluidTags.toList()
        this.itemTags = itemTags.toList()
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
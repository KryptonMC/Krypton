package org.kryptonmc.krypton.registry.tags

import kotlinx.serialization.Serializable
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey

data class Tag(val name: NamespacedKey, val values: MutableSet<NamespacedKey> = mutableSetOf()) {

    constructor(manager: TagManager, name: NamespacedKey, type: String, previous: Tag, data: TagData) : this(name) {
        if (!data.replace) values += previous.values
        data.values.forEach {
            if (it.startsWith('#')) {
                val subTag = manager.load(it.drop(1).toNamespacedKey(), type)
                values += subTag.values
            } else {
                values += it.toNamespacedKey()
            }
        }
    }

    companion object {

        val EMPTY = Tag(NamespacedKey(value = "krypton:empty"))
    }
}

data class RequiredTag(
    val type: TagType,
    val name: NamespacedKey
)

@Serializable
data class TagData(
    val replace: Boolean,
    val values: List<String>
)

enum class TagType {

    BLOCKS,
    ITEMS,
    FLUIDS,
    ENTITY_TYPES
}
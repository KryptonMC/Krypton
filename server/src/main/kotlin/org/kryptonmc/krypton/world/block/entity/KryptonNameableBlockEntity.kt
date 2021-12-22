package org.kryptonmc.krypton.world.block.entity

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.block.entity.NameableBlockEntity
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.spongepowered.math.vector.Vector3i

abstract class KryptonNameableBlockEntity(
    type: BlockEntityType,
    position: Vector3i,
    block: Block,
    override val translation: TranslatableComponent
) : KryptonBlockEntity(type, position, block), NameableBlockEntity {

    private var name: Component? = null
    override var displayName: Component
        get() = name ?: translation
        set(value) {
            name = value
        }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        if (tag.contains("CustomName", StringTag.ID)) name = GsonComponentSerializer.gson().deserialize(tag.getString("CustomName"))
    }

    override fun saveAdditional(tag: CompoundTag.Builder): CompoundTag.Builder = super.saveAdditional(tag).apply {
        if (name != null) string("CustomName", GsonComponentSerializer.gson().serialize(name!!))
    }
}

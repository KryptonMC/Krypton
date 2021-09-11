/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.item.meta

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.meta.MetaKey
import org.kryptonmc.api.item.meta.MetaKeys
import org.kryptonmc.krypton.item.ItemFlag
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.MutableCompoundTag
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.StringTag
import java.awt.Color

object MetaFactory {

    private val MAP = mapOf(
        MetaKeys.DAMAGE to ItemMetaSerializer(
            { it.getInt("Damage") },
            { nbt, damage -> nbt.putInt("Damage", damage) },
            { it.contains("Damage", IntTag.ID) }
        ),
        MetaKeys.UNBREAKABLE to ItemMetaSerializer(
            { it.getBoolean("Unbreakable") },
            { nbt, unbreakable -> nbt.putBoolean("Unbreakable", unbreakable) },
            { it.contains("Unbreakable", ByteTag.ID) }
        ),
        MetaKeys.CAN_DESTROY to BLOCK_LIST_META_SERIALIZER,
        MetaKeys.CUSTOM_MODEL_DATA to ItemMetaSerializer(
            { it.getInt("CustomModelData") },
            { nbt, data -> nbt.putInt("CustomModelData", data) },
            { it.contains("CustomModelData", IntTag.ID) }
        ),
        MetaKeys.CAN_PLACE_ON to BLOCK_LIST_META_SERIALIZER,
        MetaKeys.NAME to ItemMetaSerializer(
            { GsonComponentSerializer.gson().deserialize(it.getCompound("display").getString("Name")) },
            { nbt, name -> nbt.update("display") { putString("Name", name.toJsonString()) } },
            { it.contains("display", CompoundTag.ID) &&
                    it.getCompound("display").contains("Name", StringTag.ID) }
        ),
        MetaKeys.LORE to ItemMetaSerializer(
            { nbt -> nbt.getCompound("display").getList("Lore", StringTag.ID)
                .map { GsonComponentSerializer.gson().deserialize((it as StringTag).value) }
            },
            { nbt, lore ->
                nbt.update("display") {
                    put(
                        "Lore",
                        MutableListTag(lore.mapTo(mutableListOf()) { StringTag.of(it.toJsonString()) }, StringTag.ID)
                    )
                }
            },
            { it.contains("display", CompoundTag.ID) &&
                    it.getCompound("display").contains("Lore", StringTag.ID) }
        ),
        MetaKeys.COLOR to ItemMetaSerializer(
            { Color(it.getCompound("display").getInt("color")) },
            { nbt, color -> nbt.update("display") { putInt("color", color.rgb) } },
            { it.contains("display", CompoundTag.ID) &&
                    it.getCompound("display").contains("color", IntTag.ID) }
        ),
        MetaKeys.HIDE_ATTRIBUTES to hideFlagsSerializer(ItemFlag.ATTRIBUTES),
        MetaKeys.HIDE_CAN_DESTROY to hideFlagsSerializer(ItemFlag.CAN_DESTROY),
        MetaKeys.HIDE_CAN_PLACE_ON to hideFlagsSerializer(ItemFlag.CAN_PLACE),
        MetaKeys.HIDE_DYE to hideFlagsSerializer(ItemFlag.DYE),
        MetaKeys.HIDE_ENCHANTMENTS to hideFlagsSerializer(ItemFlag.ENCHANTMENTS),
        MetaKeys.HIDE_MISCELLANEOUS to hideFlagsSerializer(ItemFlag.MISCELLANEOUS),
        MetaKeys.HIDE_UNBREAKABLE to hideFlagsSerializer(ItemFlag.UNBREAKABLE)
    )

    @Suppress("UNCHECKED_CAST")
    fun <V : Any> get(key: MetaKey<V>, nbt: CompoundTag): V? = (MAP[key] as? ItemMetaSerializer<V>)?.reader?.invoke(nbt)

    @Suppress("UNCHECKED_CAST")
    fun <V : Any> set(key: MetaKey<V>, nbt: MutableCompoundTag, value: V) {
        val serializer = MAP[key] as? ItemMetaSerializer<V> ?: return
        serializer.writer.invoke(nbt, value)
    }

    fun <V : Any> contains(key: MetaKey<V>, nbt: CompoundTag): Boolean = MAP[key]?.predicate?.invoke(nbt) ?: false
}

private fun hideFlagsSerializer(flag: ItemFlag) = ItemMetaSerializer(
    { it.getFlag(flag) },
    { nbt, value -> nbt.setFlag(flag, value) },
    { it.getFlag(flag) }
)

private val BLOCK_LIST_META_SERIALIZER = ItemMetaSerializer(
    { nbt ->
        val blocks = mutableListOf<Block>()
        nbt.getList("CanDestroy", StringTag.ID).forEachString { key ->
            BlockLoader.fromKey(key)?.let { blocks.add(it) }
        }
        blocks
    },
    { nbt, blocks ->
        nbt["CanDestroy"] = MutableListTag(blocks.mapTo(mutableListOf()) { StringTag.of(it.key.asString()) })
    },
    { it.contains("CanDestroy", ListTag.ID) }
)

private fun CompoundTag.getFlag(flag: ItemFlag) =
    contains("HideFlags", 99) && getInt("HideFlags") and flag.mask == 0

private fun MutableCompoundTag.setFlag(flag: ItemFlag, value: Boolean) {
    var flags = getInt("HideFlags")
    if (value) {
        putInt("HideFlags", flags or flag.mask)
    } else {
        flags = flags and flag.mask.inv()
        if (flags == 0) remove("HideFlags") else putInt("HideFlags", flags)
    }
}

class ItemMetaSerializer<V : Any>(
    val reader: (CompoundTag) -> V,
    val writer: (MutableCompoundTag, V) -> Unit,
    val predicate: (CompoundTag) -> Boolean
)

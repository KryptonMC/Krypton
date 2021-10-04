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

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.registry.Registries
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

object KryptonMetaKeys {

    val DAMAGE = register(
        "damage",
        { it.getInt("Damage") },
        { nbt, damage -> nbt.putInt("Damage", damage) },
        { it.contains("Damage", IntTag.ID) }
    )
    val UNBREAKABLE = register(
        "unbreakable",
        { it.getBoolean("Unbreakable") },
        { nbt, unbreakable -> nbt.putBoolean("Unbreakable", unbreakable) },
        { it.contains("Unbreakable", ByteTag.ID) }
    )
    val CAN_DESTROY = register("can_destroy", blockList("CanDestroy"), setBlockList("CanDestroy")) {
        it.contains("CanDestroy", ListTag.ID)
    }
    val CUSTOM_MODEL_DATA = register(
        "custom_model_data",
        { it.getInt("CustomModelData") },
        { nbt, data -> nbt.putInt("CustomModelData", data) },
        { it.contains("CustomModelData", IntTag.ID) }
    )
    val CAN_PLACE_ON = register("can_place_on", blockList("CanPlaceOn"), setBlockList("CanPlaceOn")) {
        it.contains("CanPlaceOn", ListTag.ID)
    }
    val NAME = register(
        "name",
        { GsonComponentSerializer.gson().deserialize(it.getCompound("display").getString("Name")) },
        { nbt, name -> nbt.update("display") { putString("Name", name.toJsonString()) } },
        { it.contains("display", CompoundTag.ID) && it.getCompound("display").contains("Name", StringTag.ID) }
    )
    val LORE = register(
        "lore",
        { nbt ->
            nbt.getCompound("display")
                .getList("Lore", StringTag.ID)
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
        { it.contains("display", CompoundTag.ID) && it.getCompound("display").contains("Lore", StringTag.ID) }
    )
    val COLOR = register(
        "color",
        { Color(it.getCompound("display").getInt("color")) },
        { nbt, color -> nbt.update("display") { putInt("color", color.rgb) } },
        { it.contains("display", CompoundTag.ID) && it.getCompound("display").contains("color", IntTag.ID) }
    )
    val HIDE_ATTRIBUTES = register(
        "hide_attributes",
        flag(ItemFlag.ATTRIBUTES),
        setFlag(ItemFlag.ATTRIBUTES),
        flag(ItemFlag.ATTRIBUTES)
    )
    val HIDE_CAN_DESTROY = register(
        "hide_can_destroy",
        flag(ItemFlag.CAN_DESTROY),
        setFlag(ItemFlag.CAN_DESTROY),
        flag(ItemFlag.CAN_DESTROY)
    )
    val HIDE_CAN_PLACE_ON = register(
        "hide_can_place_on",
        flag(ItemFlag.CAN_PLACE),
        setFlag(ItemFlag.CAN_PLACE),
        flag(ItemFlag.CAN_PLACE)
    )
    val HIDE_DYE = register("hide_dye", flag(ItemFlag.DYE), setFlag(ItemFlag.DYE), flag(ItemFlag.DYE))
    val HIDE_ENCHANTMENTS = register(
        "hide_enchantments",
        flag(ItemFlag.ENCHANTMENTS),
        setFlag(ItemFlag.ENCHANTMENTS),
        flag(ItemFlag.ENCHANTMENTS)
    )
    val HIDE_MISCELLANEOUS = register(
        "hide_miscellaneous",
        flag(ItemFlag.MISCELLANEOUS),
        setFlag(ItemFlag.MISCELLANEOUS),
        flag(ItemFlag.MISCELLANEOUS)
    )
    val HIDE_UNBREAKABLE = register(
        "hide_unbreakable",
        flag(ItemFlag.UNBREAKABLE),
        setFlag(ItemFlag.UNBREAKABLE),
        flag(ItemFlag.UNBREAKABLE)
    )

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    private inline fun <reified V : Any> register(
        name: String,
        noinline reader: (CompoundTag) -> V,
        noinline writer: (MutableCompoundTag, V) -> Unit,
        noinline predicate: (CompoundTag) -> Boolean
    ): KryptonMetaKey<V> {
        val key = Key.key("krypton", name)
        return Registries.register(
            Registries.META_KEYS,
            key,
            KryptonMetaKey(key, V::class.java, reader, writer, predicate)
        ) as KryptonMetaKey<V>
    }

    @JvmStatic
    private fun blockList(name: String): (CompoundTag) -> List<Block> = { nbt ->
        val blocks = mutableListOf<Block>()
        nbt.getList(name, StringTag.ID).forEachString { key -> BlockLoader.fromKey(key)?.let { blocks.add(it) } }
        blocks
    }

    private fun setBlockList(name: String): (MutableCompoundTag, List<Block>) -> Unit = { nbt, blocks ->
        nbt[name] = MutableListTag(blocks.mapTo(mutableListOf()) { StringTag.of(it.key().asString()) })
    }

    @JvmStatic
    private fun flag(flag: ItemFlag): (CompoundTag) -> Boolean = {
        it.contains("HideFlags", 99) && it.getInt("HideFlags") and flag.mask == 0
    }

    private fun setFlag(flag: ItemFlag): (MutableCompoundTag, Boolean) -> Unit = { nbt, value ->
        var flags = nbt.getInt("HideFlags")
        if (value) {
            nbt.putInt("HideFlags", flags or flag.mask)
        } else {
            flags = flags and flag.mask.inv()
            if (flags == 0) nbt.remove("HideFlags") else nbt.putInt("HideFlags", flags)
        }
    }
}

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
import net.kyori.adventure.text.Component
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
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.StringTag
import java.awt.Color
import java.util.function.Predicate

object KryptonMetaKeys {

    @JvmField val DAMAGE: KryptonMetaKey<Int> = register(
        "damage",
        { it.getInt("Damage") },
        { nbt, damage -> nbt.putInt("Damage", damage) },
        { it.contains("Damage", IntTag.ID) }
    )
    @JvmField val UNBREAKABLE: KryptonMetaKey<Boolean> = register(
        "unbreakable",
        { it.getBoolean("Unbreakable") },
        { nbt, unbreakable -> nbt.putBoolean("Unbreakable", unbreakable) },
        { it.contains("Unbreakable", ByteTag.ID) }
    )
    @JvmField val CAN_DESTROY: KryptonMetaKey<List<Block>> = register(
        "can_destroy",
        blockList("CanDestroy"),
        setBlockList("CanDestroy")
    ) { it.contains("CanDestroy", ListTag.ID) }
    @JvmField val CUSTOM_MODEL_DATA: KryptonMetaKey<Int> = register(
        "custom_model_data",
        { it.getInt("CustomModelData") },
        { nbt, data -> nbt.putInt("CustomModelData", data) },
        { it.contains("CustomModelData", IntTag.ID) }
    )
    @JvmField val CAN_PLACE_ON: KryptonMetaKey<List<Block>> = register(
        "can_place_on",
        blockList("CanPlaceOn"),
        setBlockList("CanPlaceOn")
    ) { it.contains("CanPlaceOn", ListTag.ID) }
    @JvmField val NAME: KryptonMetaKey<Component> = register(
        "name",
        { GsonComponentSerializer.gson().deserialize(it.getCompound("display").getString("Name")) },
        { nbt, name -> nbt.update("display") { putString("Name", name.toJsonString()) } },
        { it.contains("display", CompoundTag.ID) && it.getCompound("display").contains("Name", StringTag.ID) }
    )
    @JvmField val LORE: KryptonMetaKey<List<Component>> = register(
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
    @JvmField val COLOR: KryptonMetaKey<Color> = register(
        "color",
        { Color(it.getCompound("display").getInt("color")) },
        { nbt, color -> nbt.update("display") { putInt("color", color.rgb) } },
        { it.contains("display", CompoundTag.ID) && it.getCompound("display").contains("color", IntTag.ID) }
    )
    @JvmField val HIDE_ATTRIBUTES: KryptonMetaKey<Boolean> = register(
        "hide_attributes",
        flag(ItemFlag.ATTRIBUTES),
        setFlag(ItemFlag.ATTRIBUTES),
        hasFlag(ItemFlag.ATTRIBUTES)
    )
    @JvmField val HIDE_CAN_DESTROY: KryptonMetaKey<Boolean> = register(
        "hide_can_destroy",
        flag(ItemFlag.CAN_DESTROY),
        setFlag(ItemFlag.CAN_DESTROY),
        hasFlag(ItemFlag.CAN_DESTROY)
    )
    @JvmField val HIDE_CAN_PLACE_ON: KryptonMetaKey<Boolean> = register(
        "hide_can_place_on",
        flag(ItemFlag.CAN_PLACE),
        setFlag(ItemFlag.CAN_PLACE),
        hasFlag(ItemFlag.CAN_PLACE)
    )
    @JvmField val HIDE_DYE: KryptonMetaKey<Boolean> = register(
        "hide_dye",
        flag(ItemFlag.DYE),
        setFlag(ItemFlag.DYE),
        hasFlag(ItemFlag.DYE)
    )
    @JvmField val HIDE_ENCHANTMENTS: KryptonMetaKey<Boolean> = register(
        "hide_enchantments",
        flag(ItemFlag.ENCHANTMENTS),
        setFlag(ItemFlag.ENCHANTMENTS),
        hasFlag(ItemFlag.ENCHANTMENTS)
    )
    @JvmField val HIDE_MISCELLANEOUS: KryptonMetaKey<Boolean> = register(
        "hide_miscellaneous",
        flag(ItemFlag.MISCELLANEOUS),
        setFlag(ItemFlag.MISCELLANEOUS),
        hasFlag(ItemFlag.MISCELLANEOUS)
    )
    @JvmField val HIDE_UNBREAKABLE: KryptonMetaKey<Boolean> = register(
        "hide_unbreakable",
        flag(ItemFlag.UNBREAKABLE),
        setFlag(ItemFlag.UNBREAKABLE),
        hasFlag(ItemFlag.UNBREAKABLE)
    )

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    private inline fun <reified V : Any> register(
        name: String,
        reader: KryptonMetaKey.Reader<V>,
        writer: KryptonMetaKey.Writer<V>,
        predicate: Predicate<CompoundTag>
    ): KryptonMetaKey<V> {
        val key = Key.key("krypton", name)
        return Registries.META_KEYS.register(key, KryptonMetaKey(key, V::class.java, reader, writer, predicate))
    }

    @JvmStatic
    private fun blockList(name: String): KryptonMetaKey.Reader<List<Block>> = KryptonMetaKey.Reader { nbt ->
        val blocks = mutableListOf<Block>()
        nbt.getList(name, StringTag.ID).forEachString { key -> BlockLoader.fromKey(key)?.let { blocks.add(it) } }
        blocks
    }

    private fun setBlockList(name: String): KryptonMetaKey.Writer<List<Block>> = KryptonMetaKey.Writer { nbt, blocks ->
        nbt[name] = MutableListTag(blocks.mapTo(mutableListOf()) { StringTag.of(it.key().asString()) })
    }

    @JvmStatic
    private fun flag(flag: ItemFlag): KryptonMetaKey.Reader<Boolean> = KryptonMetaKey.Reader {
        it.contains("HideFlags", 99) && it.getInt("HideFlags") and flag.mask == 0
    }

    @JvmStatic
    private fun hasFlag(flag: ItemFlag): Predicate<CompoundTag> = Predicate { flag(flag).read(it) }

    private fun setFlag(flag: ItemFlag): KryptonMetaKey.Writer<Boolean> = KryptonMetaKey.Writer { nbt, value ->
        var flags = nbt.getInt("HideFlags")
        if (value) {
            nbt.putInt("HideFlags", flags or flag.mask)
        } else {
            flags = flags and flag.mask.inv()
            if (flags == 0) nbt.remove("HideFlags") else nbt.putInt("HideFlags", flags)
        }
    }
}

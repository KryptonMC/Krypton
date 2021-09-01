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
package org.kryptonmc.krypton.util.converter.versions

import ca.spottedleaf.dataconverter.converters.datatypes.DataHook
import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.ObjectType
import com.google.common.base.Splitter
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.StringDataConverter
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import org.kryptonmc.krypton.util.converter.walkers.ItemListsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.ItemsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.TileEntitiesDataWalker
import org.kryptonmc.krypton.util.converter.walkers.TypePathsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converter.walkers.convertKeys
import org.kryptonmc.krypton.util.converter.walkers.convertList
import org.kryptonmc.krypton.util.converters.ChunkFlatteningConverter
import org.kryptonmc.krypton.util.converters.EntityFlatteningConverter
import org.kryptonmc.krypton.util.converters.ItemStackFlatteningConverter
import org.kryptonmc.krypton.util.converters.SpawnEggFlatteningConverter
import org.kryptonmc.krypton.util.converters.StatsFlatteningConverter
import org.kryptonmc.krypton.util.converters.helpers.BlockFlatteningHelper
import org.kryptonmc.krypton.util.converters.helpers.ItemNameHelper
import org.kryptonmc.krypton.util.datafix.fixes.BlockStateData.tag
import org.kryptonmc.krypton.util.datafix.fixes.entity.EntityBlockStateFix.Companion.toBlockID
import org.kryptonmc.krypton.util.toKeyOrNull

object V1451 {

    private const val VERSION = MCVersions.V17W47A

    fun register() {
        // V0
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:trapped_chest", ItemListsDataWalker("Items"))

        // V1
        MCTypeRegistry.CHUNK.addStructureConverter(ChunkFlatteningConverter)
        MCTypeRegistry.CHUNK.addStructureWalker(VERSION, 1) { data, fromVersion, toVersion ->
            val level = data.getMap<String>("Level") ?: return@addStructureWalker null
            level.convertList(MCTypeRegistry.ENTITY, "Entities", fromVersion, toVersion)
            level.convertList(MCTypeRegistry.TILE_ENTITY, "TileEntities", fromVersion, toVersion)

            level.getList("TileTicks", ObjectType.MAP)?.let {
                for (i in 0 until it.size()) {
                    it.getMap<String>(i).convert(MCTypeRegistry.BLOCK_NAME, "i", fromVersion, toVersion)
                }
            }

            level.getList("Sections", ObjectType.MAP)?.let {
                for (i in 0 until it.size()) {
                    it.getMap<String>(i).convertList(MCTypeRegistry.BLOCK_STATE, "Palette", fromVersion, toVersion)
                }
            }
            null
        }

        // V2
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:piston", VERSION, 2) { data, _, _ ->
            val blockId = data.getInt("blockId")
            val blockData = data.getInt("blockData") and 15

            data.remove("blockId")
            data.remove("blockData")
            // copy to avoid problems with later state data converters
            data.setMap("blockState", BlockFlatteningHelper.getNBTForId((blockId shl 4) or blockData).copy())
            null
        }
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, 2, "minecraft:piston", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("blockState")))

        // V3
        EntityFlatteningConverter.register()
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:filled_map", VERSION, 3) { data, _, _ ->
            val tag = data.getMap("tag") ?: NBTTypeUtil.createEmptyMap<String>().apply { data.setMap("tag", this) }
            if (!tag.hasKey("map", ObjectType.NUMBER)) { // This if is from CB. as usual, no documentation from CB. I'm guessing it just wants to avoid possibly overwriting it. seems fine.
                tag.setInt("map", data.getInt("Damage"))
            }
            null
        }
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:potion", ItemsDataWalker("Potion"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:arrow", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("inBlockState")))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:enderman", ItemListsDataWalker("ArmorItems", "HandItems"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:enderman", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("carriedBlockState")))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:falling_block", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("BlockState")))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:falling_block", TileEntitiesDataWalker("TileEntityData"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:spectral_arrow", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("inBlockState")))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:chest_minecart", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState")))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:chest_minecart", ItemListsDataWalker("Items"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:commandblock_minecart", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState")))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:furnace_minecart", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState")))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:hopper_minecart", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState")))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:hopper_minecart", ItemListsDataWalker("Items"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:minecart", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState")))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:spawner_minecart", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState")))
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:spawner_minecart") { data, fromVersion, toVersion ->
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion)
            null
        }
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:tnt_minecart", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState")))

        // V4
        MCTypeRegistry.BLOCK_NAME.addConverter(VERSION, 4) { data, _, _ ->
            when (data) {
                is Number -> BlockFlatteningHelper.getNameForId(data.toInt())
                is String -> BlockFlatteningHelper.getNewBlockName(data)
                else -> null
            }
        }
        MCTypeRegistry.ITEM_STACK.addStructureConverter(ItemStackFlatteningConverter)

        // V5
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:spawn_egg", SpawnEggFlatteningConverter)
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:banner", VERSION, 5) { data, _, _ ->
            val base = data.getNumber("Base")
            if (base != null) data.setInt("Base", 15 - base.toInt())

            data.getList("Patterns", ObjectType.MAP)?.let { patterns ->
                for (i in 0 until patterns.size()) {
                    val pattern = patterns.getMap<String>(i)
                    pattern.getNumber("Color")?.let { pattern.setInt("Color", 15 - it.toInt()) }
                }
            }
            null
        }
        MCTypeRegistry.LEVEL.addStructureConverter(object : StringDataConverter(VERSION, 5) {
            private val SPLITTER = Splitter.on(';').limit(5)
            private val LAYER_SPLITTER = Splitter.on(',')
            private val OLD_AMOUNT_SPLITTER = Splitter.on('x').limit(2)
            private val AMOUNT_SPLITTER = Splitter.on('*').limit(2)
            private val BLOCK_SPLITTER = Splitter.on(':').limit(3)

            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                if (data.getString("generatorName") != "flat") return null
                val generatorOptions = data.getString("generatorOptions") ?: return null
                data.setString("generatorOptions", generatorOptions.fixGeneratorSettings())
                return null
            }

            // I rewrote this for my DFU fixes port, so this is what I'm using here
            private fun String.fixGeneratorSettings(): String {
                if (isEmpty()) return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village"
                val iterator = SPLITTER.split(this).iterator()
                val first = iterator.next()
                val version = if (iterator.hasNext()) first.toIntOrNull() ?: 0 else 0
                val next = if (iterator.hasNext()) iterator.next() else first
                if (version !in 0..3) return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village"
                val builder = StringBuilder()
                val splitter = if (version < 3) OLD_AMOUNT_SPLITTER else AMOUNT_SPLITTER
                builder.append(LAYER_SPLITTER.split(next).asSequence().map {
                    val amounts = splitter.splitToList(it)
                    val amount = if (amounts.size == 2) amounts[0].toIntOrNull() ?: 0 else 1
                    val blocksString = if (amounts.size == 2) amounts[1] else amounts[0]
                    val blocks = BLOCK_SPLITTER.splitToList(blocksString)
                    val index = if (blocks.first() == "minecraft") 1 else 0
                    val block = blocks[index]
                    val blockId = if (version == 3) "minecraft:$block".toBlockID() else block.toIntOrNull() ?: 0
                    val nextIndex = index + 1
                    val id = blocks.getOrNull(nextIndex)?.toIntOrNull() ?: 0
                    (if (amount == 1) "" else "$amount*") + (blockId shl 4 or id).tag()!!["Name"].asString("")
                })
                while (iterator.hasNext()) builder.append(";").append(iterator.next())
                return builder.toString()
            }
        })

        // V6
        MCTypeRegistry.STATS.addStructureConverter(StatsFlatteningConverter)
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:jukebox", VERSION, 6) { data, _, _ ->
            val record = data.getInt("Record")
            if (record <= 0) return@addConverterForId null
            data.remove("Record")

            val newItemId = ItemStackFlatteningConverter.flattenItem(ItemNameHelper.getNameFromId(record)!!, 0) ?: return@addConverterForId null
            val recordItem = NBTTypeUtil.createEmptyMap<String>()
            data.setMap("RecordItem", recordItem)

            recordItem.setString("id", newItemId)
            recordItem.setByte("Count", 1)
            null
        }
        MCTypeRegistry.STATS.addStructureWalker(VERSION, 6) { data, fromVersion, toVersion ->
            val stats = data.getMap<String>("stats") ?: return@addStructureWalker null
            stats.convertKeys(MCTypeRegistry.BLOCK_NAME, "minecraft:mined", fromVersion, toVersion)

            stats.convertKeys(MCTypeRegistry.ITEM_NAME, "minecraft:crafted", fromVersion, toVersion)
            stats.convertKeys(MCTypeRegistry.ITEM_NAME, "minecraft:used", fromVersion, toVersion)
            stats.convertKeys(MCTypeRegistry.ITEM_NAME, "minecraft:broken", fromVersion, toVersion)
            stats.convertKeys(MCTypeRegistry.ITEM_NAME, "minecraft:picked_up", fromVersion, toVersion)
            stats.convertKeys(MCTypeRegistry.ITEM_NAME, "minecraft:dropped", fromVersion, toVersion)

            stats.convertKeys(MCTypeRegistry.ENTITY_NAME, "minecraft:killed", fromVersion, toVersion)
            stats.convertKeys(MCTypeRegistry.ENTITY_NAME, "minecraft:killed_by", fromVersion, toVersion)
            null
        }
        MCTypeRegistry.OBJECTIVE.addStructureHook(VERSION, 6, object : DataHook<MapType<String>, MapType<String>> {

            override fun preHook(data: MapType<String>, fromVersion: Long, toVersion: Long): MapType<String>? {
                // unpack
                val criteriaName = data.getString("CriteriaName")
                var type: String?
                var id: String?

                if (criteriaName != null) {
                    val index = criteriaName.indexOf(':')
                    if (index < 0) {
                        type = "_special"
                        id = criteriaName
                    } else try {
                        type = Key.key(criteriaName.substring(0, index), '.').asString()
                        id = Key.key(criteriaName.substring(index + 1), '.').asString()
                    } catch (exception: Exception) {
                        type = "_special"
                        id = criteriaName
                    }
                } else {
                    type = null
                    id = null
                }

                if (type != null && id != null) {
                    val criteriaType = NBTTypeUtil.createEmptyMap<String>()
                    data.setMap("CriteriaType", criteriaType)
                    criteriaType.setString("type", type)
                    criteriaType.setString("id", id)
                }
                return null
            }

            override fun postHook(data: MapType<String>, fromVersion: Long, toVersion: Long): MapType<String>? {
                // repack
                val criteriaType = data.getMap<String>("CriteriaType")

                val newName = if (criteriaType == null) {
                    null
                } else {
                    val type = criteriaType.getString("type")
                    val id = criteriaType.getString("id")
                    if (type != null && id != null) {
                        if (type == "_special") id else "${packWithDot(type)}:${packWithDot(id)}"
                    } else {
                        null
                    }
                }
                if (newName != null) {
                    data.remove("CriteriaType")
                    data.setString("CriteriaName", newName)
                }
                return null
            }

            private fun packWithDot(string: String): String = string.toKeyOrNull()?.let { "${it.namespace()}.${it.value()}" } ?: string
        })
        MCTypeRegistry.OBJECTIVE.addStructureWalker(VERSION, 6) { data, fromVersion, toVersion ->
            val criteriaType = data.getMap<String>("CriteriaType") ?: return@addStructureWalker null
            val type = criteriaType.getString("type") ?: return@addStructureWalker null
            when (type) {
                "minecraft:mined" -> criteriaType.convert(MCTypeRegistry.ITEM_NAME, "id", fromVersion, toVersion)
                "minecraft:crafted", "minecraft:used", "minecraft:broken", "minecraft:picked_up", "minecraft:dropped" -> {
                    criteriaType.convert(MCTypeRegistry.BLOCK_NAME, "id", fromVersion, toVersion)
                }
                "minecraft:killed", "minecraft:killed_by" -> criteriaType.convert(MCTypeRegistry.ENTITY_NAME, "id", fromVersion, toVersion)
            }
            null
        }

        // V7
        MCTypeRegistry.STRUCTURE_FEATURE.addStructureConverter(object : StringDataConverter(VERSION, 7) {

            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                val children = data.getList("Children", ObjectType.MAP) ?: return null
                for (i in 0 until children.size()) {
                    val child = children.getMap<String>(i)
                    when (child.getString("id")) {
                        "ViF" -> {
                            child.convertToBlockState("CA")
                            child.convertToBlockState("CB")
                        }
                        "ViDF" -> {
                            child.convertToBlockState("CA")
                            child.convertToBlockState("CB")
                            child.convertToBlockState("CC")
                            child.convertToBlockState("CD")
                        }
                    }
                }
                return null
            }

            private fun MapType<String>.convertToBlockState(path: String) {
                val number = getNumber(path) ?: return
                setMap(path, BlockFlatteningHelper.getNBTForId(number.toInt() shl 4).copy()) // copy to avoid problems with later state data converters
            }
        })
        // convert villagers to trade with pumpkins and not the carved pumpkin
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:villager", object : StringDataConverter(VERSION, 7) {

            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                data.getMap<String>("Offers")?.let { offers ->
                    offers.getList("Recipes", ObjectType.MAP)?.let {
                        for (i in 0 until it.size()) {
                            val recipe = it.getMap<String>(i)
                            recipe.convertPumpkin("buy")
                            recipe.convertPumpkin("buyB")
                            recipe.convertPumpkin("sell")
                        }
                    }
                }
                return null
            }

            private fun MapType<String>.convertPumpkin(path: String) {
                val item = getMap<String>(path) ?: return
                val id = item.getString("id")
                if (id == "minecraft:carved_pumpkin") item.setString("id", "minecraft:pumpkin")
            }
        })
        MCTypeRegistry.STRUCTURE_FEATURE.addStructureWalker(VERSION, 7) { data, fromVersion, toVersion ->
            val list = data.getList("Children", ObjectType.MAP) ?: return@addStructureWalker null
            for (i in 0 until list.size()) {
                val child = list.getMap<String>(i)
                child.convert(MCTypeRegistry.BLOCK_STATE, "CA", fromVersion, toVersion)
                child.convert(MCTypeRegistry.BLOCK_STATE, "CB", fromVersion, toVersion)
                child.convert(MCTypeRegistry.BLOCK_STATE, "CC", fromVersion, toVersion)
                child.convert(MCTypeRegistry.BLOCK_STATE, "CD", fromVersion, toVersion)
            }
            null
        }
    }
}

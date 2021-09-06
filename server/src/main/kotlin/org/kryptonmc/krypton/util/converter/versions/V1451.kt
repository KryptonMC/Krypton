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
import org.kryptonmc.krypton.util.toKeyOrNull

object V1451 {

    private const val VERSION = MCVersions.V17W47A
    private val REMAPPED_IDS = mapOf(
        "minecraft:air" to 0,
        "minecraft:stone" to 1,
        "minecraft:grass" to 2,
        "minecraft:dirt" to 3,
        "minecraft:cobblestone" to 4,
        "minecraft:planks" to 5,
        "minecraft:sapling" to 6,
        "minecraft:bedrock" to 7,
        "minecraft:flowing_water" to 8,
        "minecraft:water" to 9,
        "minecraft:flowing_lava" to 10,
        "minecraft:lava" to 11,
        "minecraft:sand" to 12,
        "minecraft:gravel" to 13,
        "minecraft:gold_ore" to 14,
        "minecraft:iron_ore" to 15,
        "minecraft:coal_ore" to 16,
        "minecraft:log" to 17,
        "minecraft:leaves" to 18,
        "minecraft:sponge" to 19,
        "minecraft:glass" to 20,
        "minecraft:lapis_ore" to 21,
        "minecraft:lapis_block" to 22,
        "minecraft:dispenser" to 23,
        "minecraft:sandstone" to 24,
        "minecraft:noteblock" to 25,
        "minecraft:bed" to 26,
        "minecraft:golden_rail" to 27,
        "minecraft:detector_rail" to 28,
        "minecraft:sticky_piston" to 29,
        "minecraft:web" to 30,
        "minecraft:tallgrass" to 31,
        "minecraft:deadbush" to 32,
        "minecraft:piston" to 33,
        "minecraft:piston_head" to 34,
        "minecraft:wool" to 35,
        "minecraft:piston_extension" to 36,
        "minecraft:yellow_flower" to 37,
        "minecraft:red_flower" to 38,
        "minecraft:brown_mushroom" to 39,
        "minecraft:red_mushroom" to 40,
        "minecraft:gold_block" to 41,
        "minecraft:iron_block" to 42,
        "minecraft:double_stone_slab" to 43,
        "minecraft:stone_slab" to 44,
        "minecraft:brick_block" to 45,
        "minecraft:tnt" to 46,
        "minecraft:bookshelf" to 47,
        "minecraft:mossy_cobblestone" to 48,
        "minecraft:obsidian" to 49,
        "minecraft:torch" to 50,
        "minecraft:fire" to 51,
        "minecraft:mob_spawner" to 52,
        "minecraft:oak_stairs" to 53,
        "minecraft:chest" to 54,
        "minecraft:redstone_wire" to 55,
        "minecraft:diamond_ore" to 56,
        "minecraft:diamond_block" to 57,
        "minecraft:crafting_table" to 58,
        "minecraft:wheat" to 59,
        "minecraft:farmland" to 60,
        "minecraft:furnace" to 61,
        "minecraft:lit_furnace" to 62,
        "minecraft:standing_sign" to 63,
        "minecraft:wooden_door" to 64,
        "minecraft:ladder" to 65,
        "minecraft:rail" to 66,
        "minecraft:stone_stairs" to 67,
        "minecraft:wall_sign" to 68,
        "minecraft:lever" to 69,
        "minecraft:stone_pressure_plate" to 70,
        "minecraft:iron_door" to 71,
        "minecraft:wooden_pressure_plate" to 72,
        "minecraft:redstone_ore" to 73,
        "minecraft:lit_redstone_ore" to 74,
        "minecraft:unlit_redstone_torch" to 75,
        "minecraft:redstone_torch" to 76,
        "minecraft:stone_button" to 77,
        "minecraft:snow_layer" to 78,
        "minecraft:ice" to 79,
        "minecraft:snow" to 80,
        "minecraft:cactus" to 81,
        "minecraft:clay" to 82,
        "minecraft:reeds" to 83,
        "minecraft:jukebox" to 84,
        "minecraft:fence" to 85,
        "minecraft:pumpkin" to 86,
        "minecraft:netherrack" to 87,
        "minecraft:soul_sand" to 88,
        "minecraft:glowstone" to 89,
        "minecraft:portal" to 90,
        "minecraft:lit_pumpkin" to 91,
        "minecraft:cake" to 92,
        "minecraft:unpowered_repeater" to 93,
        "minecraft:powered_repeater" to 94,
        "minecraft:stained_glass" to 95,
        "minecraft:trapdoor" to 96,
        "minecraft:monster_egg" to 97,
        "minecraft:stonebrick" to 98,
        "minecraft:brown_mushroom_block" to 99,
        "minecraft:red_mushroom_block" to 100,
        "minecraft:iron_bars" to 101,
        "minecraft:glass_pane" to 102,
        "minecraft:melon_block" to 103,
        "minecraft:pumpkin_stem" to 104,
        "minecraft:melon_stem" to 105,
        "minecraft:vine" to 106,
        "minecraft:fence_gate" to 107,
        "minecraft:brick_stairs" to 108,
        "minecraft:stone_brick_stairs" to 109,
        "minecraft:mycelium" to 110,
        "minecraft:waterlily" to 111,
        "minecraft:nether_brick" to 112,
        "minecraft:nether_brick_fence" to 113,
        "minecraft:nether_brick_stairs" to 114,
        "minecraft:nether_wart" to 115,
        "minecraft:enchanting_table" to 116,
        "minecraft:brewing_stand" to 117,
        "minecraft:cauldron" to 118,
        "minecraft:end_portal" to 119,
        "minecraft:end_portal_frame" to 120,
        "minecraft:end_stone" to 121,
        "minecraft:dragon_egg" to 122,
        "minecraft:redstone_lamp" to 123,
        "minecraft:lit_redstone_lamp" to 124,
        "minecraft:double_wooden_slab" to 125,
        "minecraft:wooden_slab" to 126,
        "minecraft:cocoa" to 127,
        "minecraft:sandstone_stairs" to 128,
        "minecraft:emerald_ore" to 129,
        "minecraft:ender_chest" to 130,
        "minecraft:tripwire_hook" to 131,
        "minecraft:tripwire" to 132,
        "minecraft:emerald_block" to 133,
        "minecraft:spruce_stairs" to 134,
        "minecraft:birch_stairs" to 135,
        "minecraft:jungle_stairs" to 136,
        "minecraft:command_block" to 137,
        "minecraft:beacon" to 138,
        "minecraft:cobblestone_wall" to 139,
        "minecraft:flower_pot" to 140,
        "minecraft:carrots" to 141,
        "minecraft:potatoes" to 142,
        "minecraft:wooden_button" to 143,
        "minecraft:skull" to 144,
        "minecraft:anvil" to 145,
        "minecraft:trapped_chest" to 146,
        "minecraft:light_weighted_pressure_plate" to 147,
        "minecraft:heavy_weighted_pressure_plate" to 148,
        "minecraft:unpowered_comparator" to 149,
        "minecraft:powered_comparator" to 150,
        "minecraft:daylight_detector" to 151,
        "minecraft:redstone_block" to 152,
        "minecraft:quartz_ore" to 153,
        "minecraft:hopper" to 154,
        "minecraft:quartz_block" to 155,
        "minecraft:quartz_stairs" to 156,
        "minecraft:activator_rail" to 157,
        "minecraft:dropper" to 158,
        "minecraft:stained_hardened_clay" to 159,
        "minecraft:stained_glass_pane" to 160,
        "minecraft:leaves2" to 161,
        "minecraft:log2" to 162,
        "minecraft:acacia_stairs" to 163,
        "minecraft:dark_oak_stairs" to 164,
        "minecraft:slime" to 165,
        "minecraft:barrier" to 166,
        "minecraft:iron_trapdoor" to 167,
        "minecraft:prismarine" to 168,
        "minecraft:sea_lantern" to 169,
        "minecraft:hay_block" to 170,
        "minecraft:carpet" to 171,
        "minecraft:hardened_clay" to 172,
        "minecraft:coal_block" to 173,
        "minecraft:packed_ice" to 174,
        "minecraft:double_plant" to 175,
        "minecraft:standing_banner" to 176,
        "minecraft:wall_banner" to 177,
        "minecraft:daylight_detector_inverted" to 178,
        "minecraft:red_sandstone" to 179,
        "minecraft:red_sandstone_stairs" to 180,
        "minecraft:double_stone_slab2" to 181,
        "minecraft:stone_slab2" to 182,
        "minecraft:spruce_fence_gate" to 183,
        "minecraft:birch_fence_gate" to 184,
        "minecraft:jungle_fence_gate" to 185,
        "minecraft:dark_oak_fence_gate" to 186,
        "minecraft:acacia_fence_gate" to 187,
        "minecraft:spruce_fence" to 188,
        "minecraft:birch_fence" to 189,
        "minecraft:jungle_fence" to 190,
        "minecraft:dark_oak_fence" to 191,
        "minecraft:acacia_fence" to 192,
        "minecraft:spruce_door" to 193,
        "minecraft:birch_door" to 194,
        "minecraft:jungle_door" to 195,
        "minecraft:acacia_door" to 196,
        "minecraft:dark_oak_door" to 197,
        "minecraft:end_rod" to 198,
        "minecraft:chorus_plant" to 199,
        "minecraft:chorus_flower" to 200,
        "minecraft:purpur_block" to 201,
        "minecraft:purpur_pillar" to 202,
        "minecraft:purpur_stairs" to 203,
        "minecraft:purpur_double_slab" to 204,
        "minecraft:purpur_slab" to 205,
        "minecraft:end_bricks" to 206,
        "minecraft:beetroots" to 207,
        "minecraft:grass_path" to 208,
        "minecraft:end_gateway" to 209,
        "minecraft:repeating_command_block" to 210,
        "minecraft:chain_command_block" to 211,
        "minecraft:frosted_ice" to 212,
        "minecraft:magma" to 213,
        "minecraft:nether_wart_block" to 214,
        "minecraft:red_nether_brick" to 215,
        "minecraft:bone_block" to 216,
        "minecraft:structure_void" to 217,
        "minecraft:observer" to 218,
        "minecraft:white_shulker_box" to 219,
        "minecraft:orange_shulker_box" to 220,
        "minecraft:magenta_shulker_box" to 221,
        "minecraft:light_blue_shulker_box" to 222,
        "minecraft:yellow_shulker_box" to 223,
        "minecraft:lime_shulker_box" to 224,
        "minecraft:pink_shulker_box" to 225,
        "minecraft:gray_shulker_box" to 226,
        "minecraft:silver_shulker_box" to 227,
        "minecraft:cyan_shulker_box" to 228,
        "minecraft:purple_shulker_box" to 229,
        "minecraft:blue_shulker_box" to 230,
        "minecraft:brown_shulker_box" to 231,
        "minecraft:green_shulker_box" to 232,
        "minecraft:red_shulker_box" to 233,
        "minecraft:black_shulker_box" to 234,
        "minecraft:white_glazed_terracotta" to 235,
        "minecraft:orange_glazed_terracotta" to 236,
        "minecraft:magenta_glazed_terracotta" to 237,
        "minecraft:light_blue_glazed_terracotta" to 238,
        "minecraft:yellow_glazed_terracotta" to 239,
        "minecraft:lime_glazed_terracotta" to 240,
        "minecraft:pink_glazed_terracotta" to 241,
        "minecraft:gray_glazed_terracotta" to 242,
        "minecraft:silver_glazed_terracotta" to 243,
        "minecraft:cyan_glazed_terracotta" to 244,
        "minecraft:purple_glazed_terracotta" to 245,
        "minecraft:blue_glazed_terracotta" to 246,
        "minecraft:brown_glazed_terracotta" to 247,
        "minecraft:green_glazed_terracotta" to 248,
        "minecraft:red_glazed_terracotta" to 249,
        "minecraft:black_glazed_terracotta" to 250,
        "minecraft:concrete" to 251,
        "minecraft:concrete_powder" to 252,
        "minecraft:structure_block" to 255
    )

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
        MCTypeRegistry.TILE_ENTITY.addWalker(
            VERSION,
            2,
            "minecraft:piston",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("blockState"))
        )

        // V3
        EntityFlatteningConverter.register()
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:filled_map", VERSION, 3) { data, _, _ ->
            val tag = data.getMap("tag") ?: NBTTypeUtil.createEmptyMap<String>().apply { data.setMap("tag", this) }
            if (!tag.hasKey("map", ObjectType.NUMBER)) {
                tag.setInt("map", data.getInt("Damage"))
            }
            null
        }
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:potion", ItemsDataWalker("Potion"))
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:arrow",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("inBlockState"))
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:enderman",
            ItemListsDataWalker("ArmorItems", "HandItems")
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:enderman",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("carriedBlockState"))
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:falling_block",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("BlockState"))
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:falling_block",
            TileEntitiesDataWalker("TileEntityData")
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:spectral_arrow",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("inBlockState"))
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:chest_minecart",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState"))
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:chest_minecart",
            ItemListsDataWalker("Items")
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:commandblock_minecart",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState"))
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:furnace_minecart",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState"))
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:hopper_minecart",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState"))
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:hopper_minecart",
            ItemListsDataWalker("Items")
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:minecart",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState"))
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:spawner_minecart",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState"))
        )
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:spawner_minecart"
        ) { data, fromVersion, toVersion ->
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion)
            null
        }
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            3,
            "minecraft:tnt_minecart",
            TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("DisplayState"))
        )

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
                    val prefix = if (amount == 1) "" else "$amount*"
                    val nbt = BlockFlatteningHelper.getNBTForIdRaw(blockId shl 4 or id)!!
                    "$prefix${nbt.getString("Name", "")}"
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
            val newItemId = ItemStackFlatteningConverter.flattenItem(ItemNameHelper.getNameFromId(record)!!, 0)
                ?: return@addConverterForId null
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

            private fun packWithDot(string: String): String =
                string.toKeyOrNull()?.let { "${it.namespace()}.${it.value()}" } ?: string
        })
        MCTypeRegistry.OBJECTIVE.addStructureWalker(VERSION, 6) { data, fromVersion, toVersion ->
            val criteriaType = data.getMap<String>("CriteriaType") ?: return@addStructureWalker null
            val type = criteriaType.getString("type") ?: return@addStructureWalker null
            when (type) {
                "minecraft:mined" -> criteriaType.convert(MCTypeRegistry.ITEM_NAME, "id", fromVersion, toVersion)
                "minecraft:crafted", "minecraft:used", "minecraft:broken", "minecraft:picked_up", "minecraft:dropped" -> {
                    criteriaType.convert(MCTypeRegistry.BLOCK_NAME, "id", fromVersion, toVersion)
                }
                "minecraft:killed", "minecraft:killed_by" -> criteriaType.convert(
                    MCTypeRegistry.ENTITY_NAME,
                    "id",
                    fromVersion,
                    toVersion
                )
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
                setMap(
                    path,
                    BlockFlatteningHelper.getNBTForId(number.toInt() shl 4).copy()
                )
            }
        })
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

    private fun String.toBlockID() = REMAPPED_IDS[this] ?: 0
}

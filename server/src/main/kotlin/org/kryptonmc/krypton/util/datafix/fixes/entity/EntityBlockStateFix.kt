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
package org.kryptonmc.krypton.util.datafix.fixes.entity

import com.mojang.datafixers.DSL.and
import com.mojang.datafixers.DSL.field
import com.mojang.datafixers.DSL.intType
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DSL.namedChoice
import com.mojang.datafixers.DSL.optional
import com.mojang.datafixers.DSL.or
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DSL.remainderType
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.fixes.BlockStateData.tag
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema

class EntityBlockStateFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val displayUpdater: (Typed<*>) -> Typed<*> = { it.updateBlockToBlockState("DisplayTile", "DisplayData", "DisplayState") }
        val inUpdater: (Typed<*>) -> Typed<*> = { it.updateBlockToBlockState("inTile", "inData", "inBlockState") }
        val inType = and(optional(field("inTile", named(References.BLOCK_NAME.typeName(), or(intType(), NamespacedSchema.NAMESPACED_STRING)))), remainderType())
        val updater: (Typed<*>) -> Typed<*> = { typed -> typed.update(inType.finder(), remainderType()) { it.second } }
        return fixTypeEverywhereTyped("EntityBlockStateFix", inputSchema.getType(References.ENTITY), outputSchema.getType(References.ENTITY)) { typed ->
            var temp = typed.updateEntity("minecraft:falling_block") { it.updateFallingBlock() }
            temp = temp.updateEntity("minecraft:enderman") { it.updateBlockToBlockState("carried", "carriedData", "carriedBlockState") }
            temp = temp.updateEntity("minecraft:arrow", inUpdater)
            temp = temp.updateEntity("minecraft:spectral_arrow", inUpdater)
            temp = temp.updateEntity("minecraft:egg", updater)
            temp = temp.updateEntity("minecraft:ender_pearl", updater)
            temp = temp.updateEntity("minecraft:fireball", updater)
            temp = temp.updateEntity("minecraft:potion", updater)
            temp = temp.updateEntity("minecraft:small_fireball", updater)
            temp = temp.updateEntity("minecraft:snowball", updater)
            temp = temp.updateEntity("minecraft:wither_skull", updater)
            temp = temp.updateEntity("minecraft:xp_bottle", updater)
            temp = temp.updateEntity("minecraft:commandblock_minecart", displayUpdater)
            temp = temp.updateEntity("minecraft:minecart", displayUpdater)
            temp = temp.updateEntity("minecraft:chest_minecart", displayUpdater)
            temp = temp.updateEntity("minecraft:furnace_minecart", displayUpdater)
            temp = temp.updateEntity("minecraft:tnt_minecart", displayUpdater)
            temp = temp.updateEntity("minecraft:hopper_minecart", displayUpdater)
            temp.updateEntity("minecraft:spawner_minecart", displayUpdater)
        }
    }

    private fun Typed<*>.updateFallingBlock() = run {
        val blockType = optional(field("Block", named(References.BLOCK_NAME.typeName(), or(intType(), NamespacedSchema.NAMESPACED_STRING))))
        val stateType = optional(field("BlockState", named(References.BLOCK_NAME.typeName(), remainderType())))
        val data = get(remainderFinder())
        update(blockType.finder(), stateType) { block ->
            val id = block.map({ pair -> pair.second.map({ it }, { REMAPPED_IDS[it] ?: 0 }) }, {
                val tileId = data["TileID"].asNumber().result()
                tileId.map(Number::toInt).orElseGet { data["Tile"].asByte(0).toInt() and 255 }
            })
            val intData = data["Data"].asInt(0) and 15
            Either.left(Pair.of(References.BLOCK_STATE.typeName(), (id shl 4 or intData).tag()))
        }.set(remainderFinder(), data.remove("Data").remove("TileID").remove("Tile"))
    }

    private fun Typed<*>.updateBlockToBlockState(fieldName: String, dataName: String, stateName: String) = run {
        val blockType = field(fieldName, named(References.BLOCK_NAME.typeName(), or(intType(), NamespacedSchema.NAMESPACED_STRING)))
        val stateType = field(stateName, named(References.BLOCK_STATE.typeName(), remainderType()))
        val data = getOrCreate(remainderFinder())
        update(blockType.finder(), stateType) { pair ->
            val id = pair.second.map({ it }, { REMAPPED_IDS[it] ?: 0 })
            val intData = data[dataName].asInt(0) and 15
            Pair.of(References.BLOCK_STATE.typeName(), (id shl 4 or intData).tag())
        }.set(remainderFinder(), data.remove(dataName))
    }

    private fun Typed<*>.updateEntity(name: String, updater: (Typed<*>) -> Typed<*>) = run {
        val inputType = inputSchema.getChoiceType(References.ENTITY, name)
        val outputType = outputSchema.getChoiceType(References.ENTITY, name)
        updateTyped(namedChoice(name, inputType), outputType, updater)
    }

    companion object {

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

        fun String.toBlockID() = REMAPPED_IDS[this] ?: 0
    }
}

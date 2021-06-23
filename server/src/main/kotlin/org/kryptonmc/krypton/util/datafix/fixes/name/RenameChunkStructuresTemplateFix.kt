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
package org.kryptonmc.krypton.util.datafix.fixes.name

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class RenameChunkStructuresTemplateFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val type = inputSchema.getType(References.STRUCTURE_FEATURE)
        val childrenFinder = type.findField("Children")
        return fixTypeEverywhereTyped("RenameChunkStructuresTemplateFix", type) { typed ->
            typed.updateTyped(childrenFinder) { children -> children.update(remainderFinder()) { typed[remainderFinder()].fixTag(it) } }
        }
    }

    private fun Dynamic<*>.fixTag(tag: Dynamic<*>): Dynamic<*> {
        val id = get("id").asString("")
        if (id !in RENAMES) return tag
        val renames = RENAMES.getValue(id)
        var temp = tag
        if (renames.first == tag["id"].asString("")) {
            val template = temp["Template"].asString("")
            temp = temp.set("Template", temp.createString(renames.second.getOrDefault(template, template)))
        }
        return temp
    }

    companion object {

        private val RENAMES = mapOf(
            "EndCity" to Pair("ECP", mapOf(
                "second_floor" to "second_floor_1",
                "third_floor" to "third_floor_1",
                "third_floor_c" to "third_floor_2"
            )),
            "Mansion" to Pair("WMP", mapOf(
                "carpet_south" to "carpet_south_1",
                "carpet_west" to "carpet_west_1",
                "indoors_door" to "indoors_door_1",
                "indoors_wall" to "indoors_wall_1"
            )),
            "Igloo" to Pair("Iglu", mapOf(
                "minecraft:igloo/igloo_bottom" to "minecraft:igloo/bottom",
                "minecraft:igloo/igloo_middle" to "minecraft:igloo/middle",
                "minecraft:igloo/igloo_top" to "minecraft:igloo/top"
            )),
            "Ocean_Ruin" to Pair("ORP", mapOf(
                "minecraft:ruin/big_ruin1_brick" to "minecraft:underwater_ruin/big_brick_1",
                "minecraft:ruin/big_ruin2_brick" to "minecraft:underwater_ruin/big_brick_2",
                "minecraft:ruin/big_ruin3_brick" to "minecraft:underwater_ruin/big_brick_3",
                "minecraft:ruin/big_ruin8_brick" to "minecraft:underwater_ruin/big_brick_8",
                "minecraft:ruin/big_ruin1_cracked" to "minecraft:underwater_ruin/big_cracked_1",
                "minecraft:ruin/big_ruin2_cracked" to "minecraft:underwater_ruin/big_cracked_2",
                "minecraft:ruin/big_ruin3_cracked" to "minecraft:underwater_ruin/big_cracked_3",
                "minecraft:ruin/big_ruin8_cracked" to "minecraft:underwater_ruin/big_cracked_8",
                "minecraft:ruin/big_ruin1_mossy" to "minecraft:underwater_ruin/big_mossy_1",
                "minecraft:ruin/big_ruin2_mossy" to "minecraft:underwater_ruin/big_mossy_2",
                "minecraft:ruin/big_ruin3_mossy" to "minecraft:underwater_ruin/big_mossy_3",
                "minecraft:ruin/big_ruin8_mossy" to "minecraft:underwater_ruin/big_mossy_8",
                "minecraft:ruin/big_ruin_warm4" to "minecraft:underwater_ruin/big_warm_4",
                "minecraft:ruin/big_ruin_warm5" to "minecraft:underwater_ruin/big_warm_5",
                "minecraft:ruin/big_ruin_warm6" to "minecraft:underwater_ruin/big_warm_6",
                "minecraft:ruin/big_ruin_warm7" to "minecraft:underwater_ruin/big_warm_7",
                "minecraft:ruin/ruin1_brick" to "minecraft:underwater_ruin/brick_1",
                "minecraft:ruin/ruin2_brick" to "minecraft:underwater_ruin/brick_2",
                "minecraft:ruin/ruin3_brick" to "minecraft:underwater_ruin/brick_3",
                "minecraft:ruin/ruin4_brick" to "minecraft:underwater_ruin/brick_4",
                "minecraft:ruin/ruin5_brick" to "minecraft:underwater_ruin/brick_5",
                "minecraft:ruin/ruin6_brick" to "minecraft:underwater_ruin/brick_6",
                "minecraft:ruin/ruin7_brick" to "minecraft:underwater_ruin/brick_7",
                "minecraft:ruin/ruin8_brick" to "minecraft:underwater_ruin/brick_8",
                "minecraft:ruin/ruin1_cracked" to "minecraft:underwater_ruin/cracked_1",
                "minecraft:ruin/ruin2_cracked" to "minecraft:underwater_ruin/cracked_2",
                "minecraft:ruin/ruin3_cracked" to "minecraft:underwater_ruin/cracked_3",
                "minecraft:ruin/ruin4_cracked" to "minecraft:underwater_ruin/cracked_4",
                "minecraft:ruin/ruin5_cracked" to "minecraft:underwater_ruin/cracked_5",
                "minecraft:ruin/ruin6_cracked" to "minecraft:underwater_ruin/cracked_6",
                "minecraft:ruin/ruin7_cracked" to "minecraft:underwater_ruin/cracked_7",
                "minecraft:ruin/ruin8_cracked" to "minecraft:underwater_ruin/cracked_8",
                "minecraft:ruin/ruin1_mossy" to "minecraft:underwater_ruin/mossy_1",
                "minecraft:ruin/ruin2_mossy" to "minecraft:underwater_ruin/mossy_2",
                "minecraft:ruin/ruin3_mossy" to "minecraft:underwater_ruin/mossy_3",
                "minecraft:ruin/ruin4_mossy" to "minecraft:underwater_ruin/mossy_4",
                "minecraft:ruin/ruin5_mossy" to "minecraft:underwater_ruin/mossy_5",
                "minecraft:ruin/ruin6_mossy" to "minecraft:underwater_ruin/mossy_6",
                "minecraft:ruin/ruin7_mossy" to "minecraft:underwater_ruin/mossy_7",
                "minecraft:ruin/ruin8_mossy" to "minecraft:underwater_ruin/mossy_8",
                "minecraft:ruin/ruin_warm1" to "minecraft:underwater_ruin/warm_1",
                "minecraft:ruin/ruin_warm2" to "minecraft:underwater_ruin/warm_2",
                "minecraft:ruin/ruin_warm3" to "minecraft:underwater_ruin/warm_3",
                "minecraft:ruin/ruin_warm4" to "minecraft:underwater_ruin/warm_4",
                "minecraft:ruin/ruin_warm5" to "minecraft:underwater_ruin/warm_5",
                "minecraft:ruin/ruin_warm6" to "minecraft:underwater_ruin/warm_6",
                "minecraft:ruin/ruin_warm7" to "minecraft:underwater_ruin/warm_7",
                "minecraft:ruin/ruin_warm8" to "minecraft:underwater_ruin/warm_8",
                "minecraft:ruin/big_brick_1" to "minecraft:underwater_ruin/big_brick_1",
                "minecraft:ruin/big_brick_2" to "minecraft:underwater_ruin/big_brick_2",
                "minecraft:ruin/big_brick_3" to "minecraft:underwater_ruin/big_brick_3",
                "minecraft:ruin/big_brick_8" to "minecraft:underwater_ruin/big_brick_8",
                "minecraft:ruin/big_mossy_1" to "minecraft:underwater_ruin/big_mossy_1",
                "minecraft:ruin/big_mossy_2" to "minecraft:underwater_ruin/big_mossy_2",
                "minecraft:ruin/big_mossy_3" to "minecraft:underwater_ruin/big_mossy_3",
                "minecraft:ruin/big_mossy_8" to "minecraft:underwater_ruin/big_mossy_8",
                "minecraft:ruin/big_cracked_1" to "minecraft:underwater_ruin/big_cracked_1",
                "minecraft:ruin/big_cracked_2" to "minecraft:underwater_ruin/big_cracked_2",
                "minecraft:ruin/big_cracked_3" to "minecraft:underwater_ruin/big_cracked_3",
                "minecraft:ruin/big_cracked_8" to "minecraft:underwater_ruin/big_cracked_8",
                "minecraft:ruin/big_warm_4" to "minecraft:underwater_ruin/big_warm_4",
                "minecraft:ruin/big_warm_5" to "minecraft:underwater_ruin/big_warm_5",
                "minecraft:ruin/big_warm_6" to "minecraft:underwater_ruin/big_warm_6",
                "minecraft:ruin/big_warm_7" to "minecraft:underwater_ruin/big_warm_7"
            ))
        )
    }
}

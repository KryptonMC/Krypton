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

import ca.spottedleaf.dataconverter.types.ListType
import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V2701 {

    private const val VERSION = MCVersions.V21W10A + 2
    private val INDEX_REGEX = "\\[(\\d+)]".toRegex()

    private val PIECE_TYPE = setOf(
        "minecraft:jigsaw",
        "minecraft:nvi",
        "minecraft:pcp",
        "minecraft:bastionremnant",
        "minecraft:runtime"
    )
    private val FEATURES = setOf(
        "minecraft:tree",
        "minecraft:flower",
        "minecraft:block_pile",
        "minecraft:random_patch"
    )

    fun register() = MCTypeRegistry.STRUCTURE_FEATURE.addStructureConverter(VERSION) { data, _, _ ->
        val children = data.getList("Children", ObjectType.MAP) ?: return@addStructureConverter null
        for (i in 0 until children.size()) {
            val child = children.getMap<String>(i)
            if (!PIECE_TYPE.contains(child.getString("id"))) continue
            val poolElement = child.getString("pool_element")
            if (poolElement != "minecraft:feature_pool_element") continue
            val feature = child.getMap<String>("feature") ?: continue
            feature.getString("name") ?: continue
            feature.convertToString()?.apply { child.setString("feature", this) }
        }
        null
    }

    private fun MapType<String>.convertToString() = getReplacement(
        getNestedString("type"),
        getNestedString("name"),
        getNestedString("config", "state_provider", "type"),
        getNestedString("config", "state_provider", "state", "Name"),
        getNestedString("config", "state_provider", "entries", "[0]", "data", "Name"),
        getNestedString("config", "foliage_placer", "type"),
        getNestedString("config", "leaves_provider", "state", "Name")
    )

    @Suppress("UNCHECKED_CAST")
    private fun MapType<String>.getNestedString(vararg paths: String): String {
        require(paths.isNotEmpty()) { "Missing path" }
        var current = getGeneric(paths[0])
        for (i in paths.indices) {
            val path = paths[i]
            if (!INDEX_REGEX.matches(path)) {
                current = if (current is MapType<*>) (current as MapType<String>).getGeneric(path) else null
                if (current == null) break
                continue
            }

            val match = INDEX_REGEX.matchEntire(path)!!
            val index = match.groups[1]!!.value.toInt()
            if (current !is ListType) {
                current = null
                break
            } else {
                if (index in 0 until current.size()) {
                    current = current.getGeneric(index)
                } else {
                    current = null
                    break
                }
            }
        }
        return current as? String ?: ""
    }

    private fun getReplacement(
        type: String,
        name: String,
        stateType: String,
        stateName: String,
        firstEntryName: String,
        foliageName: String,
        leavesName: String
    ): String? {
        val actualType = type.ifEmpty {
            if (name.isEmpty()) return null
            if (name == "minecraft:normal_tree") "minecraft:tree" else name
        }
        if (FEATURES.contains(actualType)) {
            if (actualType == "minecraft:random_patch") {
                if (stateType == "minecraft:simple_state_provider") {
                    if (stateName == "minecraft:sweet_berry_bush") return "minecraft:patch_berry_bush"
                    if (stateName == "minecraft:cactus") return "minecraft:patch_cactus"
                } else if (
                    stateType == "minecraft:weighted_state_provider" &&
                    (firstEntryName == "minecraft:grass" || firstEntryName == "minecraft:fern")
                ) {
                    return "minecraft:patch_taiga_grass"
                }
            } else if (actualType == "minecraft:block_pile") {
                if (stateType != "minecraft:simple_state_provider" && stateType != "minecraft:rotated_block_provider") {
                    if (stateType == "minecraft:weighted_state_provider") {
                        if (firstEntryName == "minecraft:packed_ice" || firstEntryName == "minecraft:blue_ice") {
                            return "minecraft:pile_ice"
                        }
                        if (firstEntryName == "minecraft:jack_o_lantern" || firstEntryName == "minecraft:pumpkin") {
                            return "minecraft:pile_pumpkin"
                        }
                    }
                } else {
                    if (stateName == "minecraft:hay_block") return "minecraft:pile_hay"
                    if (stateName == "minecraft:melon") return "minecraft:pile_melon"
                    if (stateName == "minecraft:snow") return "minecraft:pile_snow"
                }
            }
        } else {
            if (actualType == "minecraft:flower") return "minecraft:flower_plain"
            if (actualType == "minecraft:tree") {
                if (foliageName == "minecraft:acacia_foliage_placer") return "minecraft:acacia"
                if (foliageName == "minecraft:blob_foliage_placer" && leavesName == "minecraft:oak_leaves") return "minecraft:oak"
                if (foliageName == "minecraft:pine_foliage_placer") return "minecraft:pine"
                if (foliageName == "minecraft:spruce_foliage_placer") return "minecraft:spruce"
            }
        }
        return null
    }
}

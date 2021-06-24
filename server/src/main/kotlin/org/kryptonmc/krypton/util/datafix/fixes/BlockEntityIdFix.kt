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
package org.kryptonmc.krypton.util.datafix.fixes

import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Function

class BlockEntityIdFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    @Suppress("UNCHECKED_CAST") // This is fine
    override fun makeRule(): TypeRewriteRule {
        val oldStackType = inputSchema.getType(References.ITEM_STACK)
        val newStackType = outputSchema.getType(References.ITEM_STACK)
        val oldBlockEntityType = inputSchema.findChoiceType(References.BLOCK_ENTITY) as TaggedChoiceType<String>
        val newBlockEntityType = outputSchema.findChoiceType(References.BLOCK_ENTITY) as TaggedChoiceType<String>
        return TypeRewriteRule.seq(
            convertUnchecked("Item stack block entity name hook converter", oldStackType, newStackType),
            fixTypeEverywhere("BlockEntityIdFix", oldBlockEntityType, newBlockEntityType) { Function { pair -> pair.mapFirst { LEGACY_TO_NAMESPACED.getOrDefault(it, it) } } }
        )
    }

    companion object {

        private val LEGACY_TO_NAMESPACED = mapOf(
            "Airportal" to "minecraft:end_portal",
            "Banner" to "minecraft:banner",
            "Beacon" to "minecraft:beacon",
            "Cauldron" to "minecraft:brewing_stand",
            "Chest" to "minecraft:chest",
            "Comparator" to "minecraft:comparator",
            "Control" to "minecraft:command_block",
            "DLDetector" to "minecraft:daylight_detector",
            "Dropper" to "minecraft:dropper",
            "EnchantTable" to "minecraft:enchanting_table",
            "EndGateway" to "minecraft:end_gateway",
            "EnderChest" to "minecraft:ender_chest",
            "FlowerPot" to "minecraft:flower_pot",
            "Furnace" to "minecraft:furnace",
            "Hopper" to "minecraft:hopper",
            "MobSpawner" to "minecraft:mob_spawner",
            "Music" to "minecraft:noteblock",
            "Piston" to "minecraft:piston",
            "RecordPlayer" to "minecraft:jukebox",
            "Sign" to "minecraft:sign",
            "Skull" to "minecraft:skull",
            "Structure" to "minecraft:structure_block",
            "Trap" to "minecraft:dispenser"
        )
    }
}

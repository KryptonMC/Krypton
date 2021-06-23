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

import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.fixes.BlockStateData.tag

class BlockEntityBlockStateFix(outputSchema: Schema, changesType: Boolean) : NamedEntityFix(outputSchema, changesType, "BlockStateFix", References.BLOCK_ENTITY, "minecraft:piston") {

    override fun fix(typed: Typed<*>): Typed<*> {
        val pistonType = outputSchema.getChoiceType(References.BLOCK_ENTITY, "minecraft:piston")
        val stateType = pistonType.findFieldType("blockState")
        val stateFinder = fieldFinder("blockState", stateType)
        var data = typed[remainderFinder()]
        val blockId = data["blockId"].asInt(0)
        data = data.remove("blockId")
        val blockData = data["blockData"].asInt(0) and 15
        data = data.remove("blockData")
        val tag = (blockId shl 4 or blockData).tag() ?: error("")
        val pointTyped = pistonType.pointTyped(typed.ops).orElseThrow { IllegalStateException("Could not create new piston block entity!") }
        return pointTyped.set(remainderFinder(), data).set(stateFinder, stateType.readTyped(tag).result().orElseThrow { IllegalStateException("Could not parse newly created block state tag!") }.first)
    }
}

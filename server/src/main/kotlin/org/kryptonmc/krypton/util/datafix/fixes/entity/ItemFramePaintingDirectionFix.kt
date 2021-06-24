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

import com.mojang.datafixers.DSL.namedChoice
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class ItemFramePaintingDirectionFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val paintingType = inputSchema.getChoiceType(References.ENTITY, "Painting")
        val paintingFinder = namedChoice("Painting", paintingType)
        val frameType = inputSchema.getChoiceType(References.ENTITY, "ItemFrame")
        val frameFinder = namedChoice("ItemFrame", frameType)
        val entityType = inputSchema.getType(References.ENTITY)
        return TypeRewriteRule.seq(
            fixTypeEverywhereTyped("PaintingFix", entityType) { typed -> typed.updateTyped(paintingFinder, paintingType) { painting -> painting.update(remainderFinder()) { it.fix(isPainting = true, isFrame = false) } } },
            fixTypeEverywhereTyped("ItemFrameFix", entityType) { typed -> typed.updateTyped(frameFinder, frameType) { frame -> frame.update(remainderFinder()) { it.fix(isPainting = false, isFrame = true) } } }
        )
    }

    private fun Dynamic<*>.fix(isPainting: Boolean, isFrame: Boolean): Dynamic<*> {
        var temp = this
        if ((isPainting || isFrame) && !get("Facing").asNumber().result().isPresent) {
            val direction = if (temp["Direction"].asNumber().result().isPresent) {
                val value = temp["Direction"].asByte(0) % DIRECTIONS.size
                val directions = DIRECTIONS[value]
                temp = temp.set("TileX", temp.createInt(temp["TileX"].asInt(0) + directions[0]))
                temp = temp.set("TileY", temp.createInt(temp["TileY"].asInt(0) + directions[1]))
                temp = temp.set("TileZ", temp.createInt(temp["TileZ"].asInt(0) + directions[2]))
                temp = temp.remove("Direction")
                if (isFrame && temp["ItemRotation"].asNumber().result().isPresent) temp = temp.set("ItemRotation", temp.createByte((temp["ItemRotation"].asByte(0) * 2).toByte()))
                value
            } else {
                val value = temp["Dir"].asByte(0) % DIRECTIONS.size
                temp = temp.remove("Dir")
                value
            }.toByte()
            temp = temp.set("Facing", temp.createByte(direction))
        }
        return temp
    }

    companion object {

        private val DIRECTIONS = arrayOf(intArrayOf(0, 0, 1), intArrayOf(-1, 0, 0), intArrayOf(0, 0, -1), intArrayOf(1, 0, 0))
    }
}

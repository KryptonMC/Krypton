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
import org.kryptonmc.krypton.util.datafix.fixes.item.ItemFlatteningFix.Companion.updateItem
import org.kryptonmc.krypton.util.datafix.fixes.item.ItemIdFix.Companion.toName

class JukeboxFix(outputSchema: Schema, changesType: Boolean) : NamedEntityFix(outputSchema, changesType, "BlockEntityJukeboxFix", References.BLOCK_ENTITY, "minecraft:jukebox") {

    override fun fix(typed: Typed<*>): Typed<*> {
        val type = inputSchema.getChoiceType(References.BLOCK_ENTITY, "minecraft:jukebox")
        val recordType = type.findFieldType("RecordItem")
        val recordFinder = fieldFinder("RecordItem", recordType)
        val jukebox = typed[remainderFinder()]
        val record = jukebox["Record"].asInt(0)
        if (record > 0) {
            jukebox.remove("Record")
            val updated = record.toName().updateItem(0) ?: return typed
            var map = jukebox.emptyMap()
            map = map.set("id", map.createString(updated))
            map = map.set("Count", map.createByte(1))
            return typed.set(recordFinder, recordType.readTyped(map).result().orElseThrow { IllegalStateException("Could not create record item!") }.first)
                .set(remainderFinder(), jukebox)
        }
        return typed
    }
}

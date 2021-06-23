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

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.References

class JigsawPropertiesFix(outputSchema: Schema, changesType: Boolean) : NamedEntityFix(outputSchema, changesType, "JigsawPropertiesFix", References.BLOCK_ENTITY, "minecraft:jigsaw") {

    override fun fix(typed: Typed<*>): Typed<*> = typed.update(remainderFinder()) {
        val attachmentType = it["attachement_type"].asString("minecraft:empty")
        val targetPool = it["target_pool"].asString("minecraft:empty")
        it.set("name", it.createString(attachmentType)).set("target", it.createString(targetPool)).remove("attachement_type").set("pool", it.createString(targetPool)).remove("target_pool")
    }
}

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
package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL
import com.mojang.datafixers.DSL.compoundList
import com.mojang.datafixers.DSL.fields
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1466(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerBlockEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerBlockEntities(schema).apply {
        put("DUMMY", DSL::remainder)
    }

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(false, References.CHUNK) {
            fields("Level", optionalFields(
                "Entities", list(References.ENTITY_TREE.`in`(this)),
                "TileEntities", list(References.BLOCK_ENTITY.`in`(this)),
                "TileTicks", list(fields("i", References.BLOCK_NAME.`in`(this))),
                "Sections", list(optionalFields("Palette", list(References.BLOCK_STATE.`in`(this)))),
                "Structures", optionalFields("Starts", compoundList(References.STRUCTURE_FEATURE.`in`(this)))
            ))
        }
        registerType(false, References.STRUCTURE_FEATURE) {
            optionalFields(
                "Children", list(optionalFields(
                    "CA", References.BLOCK_STATE.`in`(this),
                    "CB", References.BLOCK_STATE.`in`(this),
                    "CC", References.BLOCK_STATE.`in`(this),
                    "CD", References.BLOCK_STATE.`in`(this)
                )),
                "biome", References.BIOME.`in`(this)
            )
        }
    }
}

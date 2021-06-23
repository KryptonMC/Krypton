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

import com.mojang.datafixers.DSL.compoundList
import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.DSL.string
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1125(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerBlockEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerBlockEntities(schema).apply {
        schema.registerSimple(this, "minecraft:bed")
    }

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(false, References.ADVANCEMENTS) {
            optionalFields(
                "minecraft:adventure/adventuring_time", optionalFields("criteria", compoundList(References.BIOME.`in`(this), constType(string()))),
                "minecraft:adventure/kill_a_mob", optionalFields("criteria", compoundList(References.ENTITY_NAME.`in`(this), constType(string()))),
                "minecraft:adventure/kill_all_mobs", optionalFields("criteria", compoundList(References.ENTITY_NAME.`in`(this), constType(string()))),
                "minecraft:husbandry/bred_all_animals", optionalFields("criteria", compoundList(References.ENTITY_NAME.`in`(this), constType(string())))
            )
        }
        registerType(false, References.BIOME) { constType(NAMESPACED_STRING) }
        registerType(false, References.ENTITY_NAME) { constType(NAMESPACED_STRING) }
    }
}

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

import com.google.common.collect.ImmutableMap
import com.mojang.datafixers.DSL
import com.mojang.datafixers.DSL.compoundList
import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.DSL.fields
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.DSL.remainder
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V2551(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(false, References.WORLD_GEN_SETTINGS) {
            fields(
                "dimensions", compoundList(constType(NAMESPACED_STRING), fields(
                    "generator", DSL.taggedChoiceLazy(
                        "type", DSL.string(), mapOf<String, Supplier<TypeTemplate>>(
                            "minecraft:debug" to Supplier { remainder() },
                            "minecraft:flat" to Supplier { optionalFields(
                                "settings", optionalFields(
                                    "biome", References.BIOME.`in`(schema),
                                    "layers", list(optionalFields("block", References.BLOCK_NAME.`in`(schema)))
                                )
                            ) },
                            "minecraft:noise" to Supplier { optionalFields(
                                "biome_source", DSL.taggedChoiceLazy("type", DSL.string(), mapOf(
                                    "minecraft:fixed" to Supplier { fields("biome", References.BIOME.`in`(schema)) },
                                    "minecraft:multi_noise" to Supplier { list(fields("biome", References.BIOME.`in`(schema))) },
                                    "minecraft:checkerboard" to Supplier { fields("biomes", list(References.BIOME.`in`(schema))) },
                                    "minecraft:vanilla_layered" to Supplier { remainder() },
                                    "minecraft:the_end" to Supplier { remainder() })
                                ),
                                "settings", DSL.or(constType(DSL.string()), optionalFields(
                                    "default_block", References.BLOCK_NAME.`in`(schema),
                                    "default_fluid", References.BLOCK_NAME.`in`(schema)
                                ))
                            ) }
                        )
                    )
                ))
            )
        }
    }
}

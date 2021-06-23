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

import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1451S3(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        schema.registerSimple(this, "minecraft:egg")
        schema.registerSimple(this, "minecraft:ender_pearl")
        schema.registerSimple(this, "minecraft:fireball")
        schema.register(this, "minecraft:potion") { _ -> optionalFields("Potion", References.ITEM_STACK.`in`(schema)) }
        schema.registerSimple(this, "minecraft:small_fireball")
        schema.registerSimple(this, "minecraft:snowball")
        schema.registerSimple(this, "minecraft:wither_skull")
        schema.registerSimple(this, "minecraft:xp_bottle")
        schema.register(this, "minecraft:arrow") { _ -> optionalFields("inBlockState", References.BLOCK_STATE.`in`(schema)) }
        schema.register(this, "minecraft:enderman") { _ -> optionalFields("carriedBlockState", References.BLOCK_STATE.`in`(schema), schema.equipment()) }
        schema.register(this, "minecraft:falling_block") { _ -> optionalFields("BlockState", References.BLOCK_STATE.`in`(schema), "TileEntityData", References.BLOCK_ENTITY.`in`(schema)) }
        schema.register(this, "minecraft:spectral_arrow") { _ -> optionalFields("inBlockState", References.BLOCK_STATE.`in`(schema)) }
        schema.register(this, "minecraft:chest_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema), "Items", list(References.ITEM_STACK.`in`(schema))) }
        schema.register(this, "minecraft:commandblock_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
        schema.register(this, "minecraft:furnace_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
        schema.register(this, "minecraft:hopper_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema), "Items", list(References.ITEM_STACK.`in`(schema))) }
        schema.register(this, "minecraft:minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
        schema.register(this, "minecraft:spawner_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema), References.UNTAGGED_SPAWNER.`in`(schema)) }
        schema.register(this, "minecraft:tnt_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
    }
}

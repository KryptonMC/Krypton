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

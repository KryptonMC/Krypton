package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1451S2(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerBlockEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerBlockEntities(schema).apply {
        schema.register(this, "minecraft:piston") { _ -> optionalFields("blockState", References.BLOCK_STATE.`in`(schema)) }
    }
}

package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import java.util.function.Supplier

class V1451(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerBlockEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerBlockEntities(schema).apply {
        schema.registerContainer(this, "minecraft:trapped_chest")
    }
}

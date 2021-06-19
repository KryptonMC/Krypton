package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import java.util.function.Supplier

class V1928(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        remove("minecraft:illager_beast")
        schema.registerMob(this, "minecraft:ravager")
    }
}

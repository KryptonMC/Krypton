package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import java.util.function.Supplier

class V143(versionKey: Int, parent: Schema?) : Schema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        remove("TippedArrow")
    }
}

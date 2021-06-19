package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1451S4(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(false, References.BLOCK_NAME) { constType(NAMESPACED_STRING) }
    }
}

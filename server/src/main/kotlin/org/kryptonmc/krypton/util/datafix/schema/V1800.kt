package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1800(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        schema.registerMob(this, "minecraft:panda")
        schema.register(this, "minecraft:pillager") { _ ->
            optionalFields("Inventory", list(References.ITEM_STACK.`in`(schema)), schema.equipment())
        }
    }
}

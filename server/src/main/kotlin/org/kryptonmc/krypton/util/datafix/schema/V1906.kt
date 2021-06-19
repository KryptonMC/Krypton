package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1906(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerBlockEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerBlockEntities(schema).apply {
        schema.registerContainer(this, "minecraft:barrel")
        schema.registerContainer(this, "minecraft:smoker")
        schema.registerContainer(this, "minecraft:blast_furnace")
        schema.register(this, "minecraft:lectern") { _ -> optionalFields("Book", References.ITEM_STACK.`in`(schema)) }
        schema.registerSimple(this, "minecraft:bell")
    }
}

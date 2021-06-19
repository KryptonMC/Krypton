package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V2100(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        schema.registerMob(this, "minecraft:bee")
        schema.registerMob(this, "minecraft:bee_stinger")
    }

    override fun registerBlockEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerBlockEntities(schema).apply {
        schema.register(this, "minecraft:beehive") { _ ->
            optionalFields("Bees", list(optionalFields("EntityData", References.ENTITY_TREE.`in`(schema))))
        }
    }
}

package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.compoundList
import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.DSL.intType
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V2501(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerBlockEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerBlockEntities(schema).apply {
        schema.registerFurnace(this, "minecraft:furnace")
        schema.registerFurnace(this, "minecraft:smoker")
        schema.registerFurnace(this, "minecraft:blast_furnace")
    }
}

private fun Schema.registerFurnace(map: Map<String, Supplier<TypeTemplate>>, name: String) = register(map, name) { _ ->
    optionalFields("Items", list(References.ITEM_STACK.`in`(this)), "RecipesUsed", compoundList(References.RECIPE.`in`(this)), constType(intType()))
}

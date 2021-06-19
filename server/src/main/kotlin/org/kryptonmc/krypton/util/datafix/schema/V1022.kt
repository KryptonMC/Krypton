package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.compoundList
import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1022(versionKey: Int, parent: Schema?) : Schema(versionKey, parent) {

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(false, References.RECIPE) { constType(NamespacedSchema.NAMESPACED_STRING) }
        registerType(false, References.PLAYER) {
            optionalFields(
                "RootVehicle", optionalFields("Entity", References.ENTITY_TREE.`in`(this)),
                "Inventory", list(References.ITEM_STACK.`in`(this)),
                "EnderItems", list(References.ITEM_STACK.`in`(this)),
                optionalFields(
                    "ShoulderEntityLeft", References.ENTITY_TREE.`in`(this),
                    "ShoulderEntityRight", References.ENTITY_TREE.`in`(this),
                    "recipeBook", optionalFields(
                        "recipes", list(References.RECIPE.`in`(this)),
                        "toBeDisplayed", list(References.RECIPE.`in`(this))
                    )
                )
            )
        }
        registerType(false, References.HOTBAR) { compoundList(list(References.ITEM_STACK.`in`(this))) }
    }
}

package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V135(versionKey: Int, parent: Schema?) : Schema(versionKey, parent) {

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(false, References.PLAYER) {
            optionalFields(
                "RootVehicle", optionalFields("Entity", References.ENTITY_TREE.`in`(this)),
                "Inventory", list(References.ITEM_STACK.`in`(this)),
                "EnderItems", list(References.ITEM_STACK.`in`(this))
            )
        }
        registerType(true, References.ENTITY_TREE) {
            optionalFields("Passengers", list(References.ENTITY_TREE.`in`(this)), References.ENTITY.`in`(this))
        }
    }
}

package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1929(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        schema.register(this, "minecraft:wandering_trader") { _ ->
            optionalFields(
                "Inventory", list(References.ITEM_STACK.`in`(schema)),
                "Offers", optionalFields("Recipes", list(
                    optionalFields(
                        "buy", References.ITEM_STACK.`in`(schema),
                        "buyB", References.ITEM_STACK.`in`(schema),
                        "sell", References.ITEM_STACK.`in`(schema),
                    )
                )),
                schema.equipment()
            )
        }
        schema.register(this, "minecraft:trader_llama") { _ ->
            optionalFields(
                "Items", list(References.ITEM_STACK.`in`(schema)),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                "DecorItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
    }
}

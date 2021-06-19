package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V703(versionKey: Int, parent: Schema?) : Schema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        remove("EntityHorse")
        register(this, "Horse") { _ ->
            optionalFields(
                "ArmorItem", References.ITEM_STACK.`in`(schema),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
        register(this, "Donkey") { _ ->
            optionalFields(
                "Items", list(References.ITEM_STACK.`in`(schema)),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
        register(this, "Mule") { _ ->
            optionalFields(
                "Items", list(References.ITEM_STACK.`in`(schema)),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
        register(this, "ZombieHorse") { _ -> optionalFields("SaddleItem", References.ITEM_STACK.`in`(schema), schema.equipment()) }
        register(this, "SkeletonHorse") { _ -> optionalFields("SaddleItem", References.ITEM_STACK.`in`(schema), schema.equipment()) }
    }
}

package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.hook
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.Hook
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V102(versionKey: Int, parent: Schema?) : Schema(versionKey, parent) {

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(true, References.ITEM_STACK) {
            hook(optionalFields(
                "id", References.ITEM_NAME.`in`(this),
                "tag", optionalFields(
                    "EntityTag", References.ENTITY_TREE.`in`(this),
                    "BlockEntityTag", References.BLOCK_ENTITY.`in`(this),
                    "CanDestroy", list(References.BLOCK_NAME.`in`(this)),
                    "CanPlaceOn", list(References.BLOCK_NAME.`in`(this)),
                    "Items", list(References.ITEM_STACK.`in`(this))
                )
            ), InitialSchema.ADD_NAMES, Hook.HookFunction.IDENTITY)
        }
    }
}

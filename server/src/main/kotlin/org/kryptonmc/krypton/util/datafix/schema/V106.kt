package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.fields
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V106(versionKey: Int, parent: Schema?) : Schema(versionKey, parent) {

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(true, References.UNTAGGED_SPAWNER) {
            optionalFields(
                "SpawnPotentials", list(fields("Entity", References.ENTITY_TREE.`in`(this))),
                "SpawnData", References.ENTITY_TREE.`in`(this)
            )
        }
    }
}

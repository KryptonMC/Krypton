package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1470(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        schema.registerMob(this, "minecraft:turtle")
        schema.registerMob(this, "minecraft:cod_mob")
        schema.registerMob(this, "minecraft:tropical_fish")
        schema.registerMob(this, "minecraft:salmon_mob")
        schema.registerMob(this, "minecraft:puffer_fish")
        schema.registerMob(this, "minecraft:phantom")
        schema.registerMob(this, "minecraft:dolphin")
        schema.registerMob(this, "minecraft:drowned")
        schema.register(this, "minecraft:trident") { _ -> optionalFields("inBlockState", References.BLOCK_STATE.`in`(schema)) }
    }
}

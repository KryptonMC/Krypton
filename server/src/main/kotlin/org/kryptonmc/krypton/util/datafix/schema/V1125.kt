package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.compoundList
import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.DSL.string
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1125(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerBlockEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerBlockEntities(schema).apply {
        schema.registerSimple(this, "minecraft:bed")
    }

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(false, References.ADVANCEMENTS) {
            optionalFields(
                "minecraft:adventure/adventuring_time", optionalFields("criteria", compoundList(References.BIOME.`in`(this), constType(string()))),
                "minecraft:adventure/kill_a_mob", optionalFields("criteria", compoundList(References.ENTITY_NAME.`in`(this), constType(string()))),
                "minecraft:adventure/kill_all_mobs", optionalFields("criteria", compoundList(References.ENTITY_NAME.`in`(this), constType(string()))),
                "minecraft:husbandry/bred_all_animals", optionalFields("criteria", compoundList(References.ENTITY_NAME.`in`(this), constType(string())))
            )
        }
        registerType(false, References.BIOME) { constType(NAMESPACED_STRING) }
        registerType(false, References.ENTITY_NAME) { constType(NAMESPACED_STRING) }
    }
}

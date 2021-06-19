package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import java.util.function.Supplier

class V1510(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        put("minecraft:command_block_minecart", remove("minecraft:commandblock_minecart"))
        put("minecraft:end_crystal", remove("minecraft:ender_crystal"))
        put("minecraft:snow_golem", remove("minecraft:snowman"))
        put("minecraft:evoker", remove("minecraft:evocation_illager"))
        put("minecraft:evoker_fangs", remove("minecraft:evocation_fangs"))
        put("minecraft:illusioner", remove("minecraft:illusion_illager"))
        put("minecraft:vindicator", remove("minecraft:vindication_illager"))
        put("minecraft:iron_golem", remove("minecraft:villager_golem"))
        put("minecraft:experience_orb", remove("minecraft:xp_orb"))
        put("minecraft:experience_bottle", remove("minecraft:xp_bottle"))
        put("minecraft:eye_of_ender", remove("minecraft:eye_of_ender_signal"))
        put("minecraft:firework_rocket", remove("minecraft:fireworks_rocket"))
    }
}

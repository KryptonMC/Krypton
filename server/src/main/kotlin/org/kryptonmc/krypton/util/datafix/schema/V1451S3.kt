package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1451S3(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        schema.registerSimple(this, "minecraft:egg")
        schema.registerSimple(this, "minecraft:ender_pearl")
        schema.registerSimple(this, "minecraft:fireball")
        schema.register(this, "minecraft:potion") { _ -> optionalFields("Potion", References.ITEM_STACK.`in`(schema)) }
        schema.registerSimple(this, "minecraft:small_fireball")
        schema.registerSimple(this, "minecraft:snowball")
        schema.registerSimple(this, "minecraft:wither_skull")
        schema.registerSimple(this, "minecraft:xp_bottle")
        schema.register(this, "minecraft:arrow") { _ -> optionalFields("inBlockState", References.BLOCK_STATE.`in`(schema)) }
        schema.register(this, "minecraft:enderman") { _ -> optionalFields("carriedBlockState", References.BLOCK_STATE.`in`(schema), schema.equipment()) }
        schema.register(this, "minecraft:falling_block") { _ -> optionalFields("BlockState", References.BLOCK_STATE.`in`(schema), "TileEntityData", References.BLOCK_ENTITY.`in`(schema)) }
        schema.register(this, "minecraft:spectral_arrow") { _ -> optionalFields("inBlockState", References.BLOCK_STATE.`in`(schema)) }
        schema.register(this, "minecraft:chest_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema), "Items", list(References.ITEM_STACK.`in`(schema))) }
        schema.register(this, "minecraft:commandblock_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
        schema.register(this, "minecraft:furnace_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
        schema.register(this, "minecraft:hopper_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema), "Items", list(References.ITEM_STACK.`in`(schema))) }
        schema.register(this, "minecraft:minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
        schema.register(this, "minecraft:spawner_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema), References.UNTAGGED_SPAWNER.`in`(schema)) }
        schema.register(this, "minecraft:tnt_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
    }
}

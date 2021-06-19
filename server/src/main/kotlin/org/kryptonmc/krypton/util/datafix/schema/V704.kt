package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL
import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.DSL.hook
import com.mojang.datafixers.DSL.intType
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.DSL.or
import com.mojang.datafixers.DSL.taggedChoiceLazy
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.Type
import com.mojang.datafixers.types.templates.Hook
import com.mojang.datafixers.types.templates.TypeTemplate
import com.mojang.serialization.Dynamic
import com.mojang.serialization.DynamicOps
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V704(versionKey: Int, parent: Schema?) : Schema(versionKey, parent) {

    override fun getChoiceType(type: DSL.TypeReference, choiceName: String): Type<*> =
        super.getChoiceType(type, if (type.typeName() == References.BLOCK_ENTITY.typeName()) choiceName.ensureNamespaced() else choiceName)

    override fun registerBlockEntities(schema: Schema) = mutableMapOf<String, Supplier<TypeTemplate>>().apply {
        schema.registerContainer(this, "minecraft:furnace")
        schema.registerContainer(this, "minecraft:chest")
        schema.registerSimple(this, "minecraft:ender_chest")
        schema.register(this, "minecraft:jukebox") { _ -> optionalFields("RecordItem", References.ITEM_STACK.`in`(schema)) }
        schema.registerContainer(this, "minecraft:dispenser")
        schema.registerContainer(this, "minecraft:dropper")
        schema.registerSimple(this, "minecraft:sign")
        schema.register(this, "minecraft:mob_spawner") { _ -> References.UNTAGGED_SPAWNER.`in`(schema) }
        schema.registerSimple(this, "minecraft:noteblock")
        schema.registerSimple(this, "minecraft:piston")
        schema.registerContainer(this, "minecraft:brewing_stand")
        schema.registerSimple(this, "minecraft:enchanting_table")
        schema.registerSimple(this, "minecraft:end_portal")
        schema.registerSimple(this, "minecraft:beacon")
        schema.registerSimple(this, "minecraft:skull")
        schema.registerSimple(this, "minecraft:daylight_detector")
        schema.registerContainer(this, "minecraft:hopper")
        schema.registerSimple(this, "minecraft:comparator")
        schema.register(this, "minecraft:flower_pot") { _ -> optionalFields("Item", or(constType(intType()), References.ITEM_STACK.`in`(schema))) }
        schema.registerSimple(this, "minecraft:banner")
        schema.registerSimple(this, "minecraft:structure_block")
        schema.registerSimple(this, "minecraft:end_gateway")
        schema.registerSimple(this, "minecraft:command_block")
    }

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(false, References.BLOCK_ENTITY) { taggedChoiceLazy("id", NamespacedSchema.NAMESPACED_STRING, blockEntityTypes) }
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
            ), ADD_NAMES, Hook.HookFunction.IDENTITY)
        }
    }

    companion object {

        val ITEM_TO_BLOCK_ENTITY = mapOf(
            "minecraft:furnace" to "minecraft:furnace",
            "minecraft:lit_furnace" to "minecraft:furnace",
            "minecraft:chest" to "minecraft:chest",
            "minecraft:trapped_chest" to "minecraft:chest",
            "minecraft:ender_chest" to "minecraft:ender_chest",
            "minecraft:jukebox" to "minecraft:jukebox",
            "minecraft:dispenser" to "minecraft:dispenser",
            "minecraft:dropper" to "minecraft:dropper",
            "minecraft:sign" to "minecraft:sign",
            "minecraft:mob_spawner" to "minecraft:mob_spawner",
            "minecraft:spawner" to "minecraft:mob_spawner",
            "minecraft:noteblock" to "minecraft:noteblock",
            "minecraft:brewing_stand" to "minecraft:brewing_stand",
            "minecraft:enhanting_table" to "minecraft:enchanting_table",
            "minecraft:command_block" to "minecraft:command_block",
            "minecraft:beacon" to "minecraft:beacon",
            "minecraft:skull" to "minecraft:skull",
            "minecraft:daylight_detector" to "minecraft:daylight_detector",
            "minecraft:hopper" to "minecraft:hopper",
            "minecraft:banner" to "minecraft:banner",
            "minecraft:flower_pot" to "minecraft:flower_pot",
            "minecraft:repeating_command_block" to "minecraft:command_block",
            "minecraft:chain_command_block" to "minecraft:command_block",
            "minecraft:shulker_box" to "minecraft:shulker_box",
            "minecraft:white_shulker_box" to "minecraft:shulker_box",
            "minecraft:orange_shulker_box" to "minecraft:shulker_box",
            "minecraft:magenta_shulker_box" to "minecraft:shulker_box",
            "minecraft:light_blue_shulker_box" to "minecraft:shulker_box",
            "minecraft:yellow_shulker_box" to "minecraft:shulker_box",
            "minecraft:lime_shulker_box" to "minecraft:shulker_box",
            "minecraft:pink_shulker_box" to "minecraft:shulker_box",
            "minecraft:gray_shulker_box" to "minecraft:shulker_box",
            "minecraft:silver_shulker_box" to "minecraft:shulker_box",
            "minecraft:cyan_shulker_box" to "minecraft:shulker_box",
            "minecraft:purple_shulker_box" to "minecraft:shulker_box",
            "minecraft:blue_shulker_box" to "minecraft:shulker_box",
            "minecraft:brown_shulker_box" to "minecraft:shulker_box",
            "minecraft:green_shulker_box" to "minecraft:shulker_box",
            "minecraft:red_shulker_box" to "minecraft:shulker_box",
            "minecraft:black_shulker_box" to "minecraft:shulker_box",
            "minecraft:bed" to "minecraft:bed",
            "minecraft:light_gray_shulker_box" to "minecraft:shulker_box",
            "minecraft:banner" to "minecraft:banner",
            "minecraft:white_banner" to "minecraft:banner",
            "minecraft:orange_banner" to "minecraft:banner",
            "minecraft:magenta_banner" to "minecraft:banner",
            "minecraft:light_blue_banner" to "minecraft:banner",
            "minecraft:yellow_banner" to "minecraft:banner",
            "minecraft:lime_banner" to "minecraft:banner",
            "minecraft:pink_banner" to "minecraft:banner",
            "minecraft:gray_banner" to "minecraft:banner",
            "minecraft:silver_banner" to "minecraft:banner",
            "minecraft:cyan_banner" to "minecraft:banner",
            "minecraft:purple_banner" to "minecraft:banner",
            "minecraft:blue_banner" to "minecraft:banner",
            "minecraft:brown_banner" to "minecraft:banner",
            "minecraft:green_banner" to "minecraft:banner",
            "minecraft:red_banner" to "minecraft:banner",
            "minecraft:black_banner" to "minecraft:banner",
            "minecraft:standing_sign" to "minecraft:sign",
            "minecraft:wall_sign" to "minecraft:sign",
            "minecraft:piston_head" to "minecraft:piston",
            "minecraft:daylight_detector_inverted" to "minecraft:daylight_detector",
            "minecraft:unpowered_comparator" to "minecraft:comparator",
            "minecraft:powered_comparator" to "minecraft:comparator",
            "minecraft:wall_banner" to "minecraft:banner",
            "minecraft:standing_banner" to "minecraft:banner",
            "minecraft:structure_block" to "minecraft:structure_block",
            "minecraft:end_portal" to "minecraft:end_portal",
            "minecraft:end_gateway" to "minecraft:end_gateway",
            "minecraft:sign" to "minecraft:sign",
            "minecraft:shield" to "minecraft:banner",
            "minecraft:white_bed" to "minecraft:bed",
            "minecraft:orange_bed" to "minecraft:bed",
            "minecraft:magenta_bed" to "minecraft:bed",
            "minecraft:light_blue_bed" to "minecraft:bed",
            "minecraft:yellow_bed" to "minecraft:bed",
            "minecraft:lime_bed" to "minecraft:bed",
            "minecraft:pink_bed" to "minecraft:bed",
            "minecraft:gray_bed" to "minecraft:bed",
            "minecraft:silver_bed" to "minecraft:bed",
            "minecraft:cyan_bed" to "minecraft:bed",
            "minecraft:purple_bed" to "minecraft:bed",
            "minecraft:blue_bed" to "minecraft:bed",
            "minecraft:brown_bed" to "minecraft:bed",
            "minecraft:green_bed" to "minecraft:bed",
            "minecraft:red_bed" to "minecraft:bed",
            "minecraft:black_bed" to "minecraft:bed",
            "minecraft:oak_sign" to "minecraft:sign",
            "minecraft:spruce_sign" to "minecraft:sign",
            "minecraft:birch_sign" to "minecraft:sign",
            "minecraft:jungle_sign" to "minecraft:sign",
            "minecraft:acacia_sign" to "minecraft:sign",
            "minecraft:dark_oak_sign" to "minecraft:sign",
            "minecraft:crimson_sign" to "minecraft:sign",
            "minecraft:warped_sign" to "minecraft:sign",
            "minecraft:skeleton_skull" to "minecraft:skull",
            "minecraft:wither_skeleton_skull" to "minecraft:skull",
            "minecraft:zombie_head" to "minecraft:skull",
            "minecraft:player_head" to "minecraft:skull",
            "minecraft:creeper_head" to "minecraft:skull",
            "minecraft:dragon_head" to "minecraft:skull",
            "minecraft:barrel" to "minecraft:barrel",
            "minecraft:conduit" to "minecraft:conduit",
            "minecraft:smoker" to "minecraft:smoker",
            "minecraft:blast_furnace" to "minecraft:blast_furnace",
            "minecraft:lectern" to "minecraft:lectern",
            "minecraft:bell" to "minecraft:bell",
            "minecraft:jigsaw" to "minecraft:jigsaw",
            "minecraft:campfire" to "minecraft:campfire",
            "minecraft:bee_nest" to "minecraft:beehive",
            "minecraft:beehive" to "minecraft:beehive",
            "minecraft:sculk_sensor" to "minecraft:sculk_sensor"
        )
        private val ADD_NAMES = object : Hook.HookFunction {
            override fun <T> apply(ops: DynamicOps<T>, value: T) = Dynamic(ops, value).addNames(ITEM_TO_BLOCK_ENTITY, "ArmorStand")
        }
    }
}

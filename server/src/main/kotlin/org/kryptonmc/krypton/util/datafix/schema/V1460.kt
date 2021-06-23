/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL
import com.mojang.datafixers.DSL.compoundList
import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.DSL.fields
import com.mojang.datafixers.DSL.hook
import com.mojang.datafixers.DSL.intType
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.DSL.string
import com.mojang.datafixers.DSL.taggedChoiceLazy
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.Hook
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V1460(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema) = mutableMapOf<String, Supplier<TypeTemplate>>().apply {
        schema.registerSimple(this, "minecraft:area_effect_cloud")
        schema.registerMob(this, "minecraft:armor_stand")
        schema.register(this, "minecraft:arrow") { _ -> optionalFields("inBlockState", References.BLOCK_STATE.`in`(schema)) }
        schema.registerMob(this, "minecraft:bat")
        schema.registerMob(this, "minecraft:blaze")
        schema.registerSimple(this, "minecraft:boat")
        schema.registerMob(this, "minecraft:cave_spider")
        schema.register(this, "minecraft:chest_minecart") { _ ->
            optionalFields(
                "DisplayState", References.BLOCK_STATE.`in`(schema),
                "Items", list(References.ITEM_STACK.`in`(schema))
            )
        }
        schema.registerMob(this, "minecraft:chicken")
        schema.register(this, "minecraft:commandblock_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
        schema.registerMob(this, "minecraft:cow")
        schema.registerMob(this, "minecraft:creeper")
        schema.register(this, "minecraft:donkey") { _ ->
            optionalFields(
                "Items", list(References.ITEM_STACK.`in`(schema)),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
        schema.registerSimple(this, "minecraft:dragon_fireball")
        schema.registerSimple(this, "minecraft:egg")
        schema.registerMob(this, "minecraft:elder_guardian")
        schema.registerSimple(this, "minecraft:ender_crystal")
        schema.registerMob(this, "minecraft:ender_dragon")
        schema.register(this, "minecraft:enderman") { _ -> optionalFields("carriedBlockState", References.BLOCK_STATE.`in`(schema), schema.equipment()) }
        schema.registerMob(this, "minecraft:endermite")
        schema.registerSimple(this, "minecraft:ender_pearl")
        schema.registerSimple(this, "minecraft:evocation_fangs")
        schema.registerMob(this, "minecraft:evocation_illager")
        schema.registerSimple(this, "minecraft:eye_of_ender_signal")
        schema.register(this, "minecraft:falling_block") { _ ->
            optionalFields(
                "BlockState", References.BLOCK_STATE.`in`(schema),
                "TileEntityData", References.BLOCK_ENTITY.`in`(schema)
            )
        }
        schema.registerSimple(this, "minecraft:fireball")
        schema.register(this, "minecraft:fireworks_rocket") { _ -> optionalFields("FireworksItem", References.ITEM_STACK.`in`(schema)) }
        schema.register(this, "minecraft:furnace_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
        schema.registerMob(this, "minecraft:ghast")
        schema.registerMob(this, "minecraft:giant")
        schema.registerMob(this, "minecraft:guardian")
        schema.register(this, "minecraft:hopper_minecart") { _ ->
            optionalFields(
                "DisplayState", References.BLOCK_STATE.`in`(schema),
                "Items", list(References.ITEM_STACK.`in`(schema))
            )
        }
        schema.register(this, "minecraft:horse") { _ ->
            optionalFields(
                "ArmorItem", References.ITEM_STACK.`in`(schema),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
        schema.registerMob(this, "minecraft:husk")
        schema.registerSimple(this, "minecraft:illusion_illager")
        schema.register(this, "minecraft:item") { _ -> optionalFields("Item", References.ITEM_STACK.`in`(schema)) }
        schema.register(this, "minecraft:item_frame") { _ -> optionalFields("Item", References.ITEM_STACK.`in`(schema)) }
        schema.registerSimple(this, "minecraft:leash_knot")
        schema.register(this, "minecraft:llama") { _ ->
            optionalFields(
                "Items", list(References.ITEM_STACK.`in`(schema)),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                "DecorItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
        schema.registerSimple(this, "minecraft:llama_spit")
        schema.registerMob(this, "minecraft:magma_cube")
        schema.register(this, "minecraft:minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
        schema.registerMob(this, "minecraft:mooshroom")
        schema.register(this, "minecraft:mule") { _ ->
            optionalFields(
                "Items", list(References.ITEM_STACK.`in`(schema)),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
        schema.registerMob(this, "minecraft:ocelot")
        schema.registerSimple(this, "minecraft:painting")
        schema.registerSimple(this, "minecraft:parrot")
        schema.registerMob(this, "minecraft:pig")
        schema.registerMob(this, "minecraft:polar_bear")
        schema.register(this, "minecraft:potion") { _ -> optionalFields("Potion", References.ITEM_STACK.`in`(schema)) }
        schema.registerMob(this, "minecraft:rabbit")
        schema.registerMob(this, "minecraft:sheep")
        schema.registerMob(this, "minecraft:shulker")
        schema.registerSimple(this, "minecraft:shulker_bullet")
        schema.registerMob(this, "minecraft:silverfish")
        schema.registerMob(this, "minecraft:skeleton")
        schema.register(this, "minecraft:skeleton_horse") { _ ->
            optionalFields("SaddleItem", References.ITEM_STACK.`in`(schema), schema.equipment())
        }
        schema.registerMob(this, "minecraft:slime")
        schema.registerSimple(this, "minecraft:small_fireball")
        schema.registerSimple(this, "minecraft:snowball")
        schema.registerMob(this, "minecraft:snowman")
        schema.register(this, "minecraft:spawner_minecart") { _ ->
            optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema), References.UNTAGGED_SPAWNER.`in`(schema))
        }
        schema.register(this, "minecraft:spectral_arrow") { _ -> optionalFields("inBlockState", References.BLOCK_STATE.`in`(schema)) }
        schema.registerMob(this, "minecraft:spider")
        schema.registerMob(this, "minecraft:squid")
        schema.registerMob(this, "minecraft:stray")
        schema.registerSimple(this, "minecraft:tnt")
        schema.register(this, "minecraft:tnt_minecart") { _ -> optionalFields("DisplayState", References.BLOCK_STATE.`in`(schema)) }
        schema.registerMob(this, "minecraft:vex")
        schema.register(this, "minecraft:villager") { _ ->
            optionalFields(
                "Inventory", list(References.ITEM_STACK.`in`(schema)),
                "Offers", optionalFields("Recipes", list(optionalFields(
                    "buy", References.ITEM_STACK.`in`(schema),
                    "buyB", References.ITEM_STACK.`in`(schema),
                    "sell", References.ITEM_STACK.`in`(schema)))
                ), schema.equipment()
            )
        }
        schema.registerMob(this, "minecraft:villager_golem")
        schema.registerMob(this, "minecraft:vindication_illager")
        schema.registerMob(this, "minecraft:witch")
        schema.registerMob(this, "minecraft:wither")
        schema.registerMob(this, "minecraft:wither_skeleton")
        schema.registerSimple(this, "minecraft:wither_skull")
        schema.registerMob(this, "minecraft:wolf")
        schema.registerSimple(this, "minecraft:xp_bottle")
        schema.registerSimple(this, "minecraft:xp_orb")
        schema.registerMob(this, "minecraft:zombie")
        schema.register(this, "minecraft:zombie_horse") { _ ->
            optionalFields("SaddleItem", References.ITEM_STACK.`in`(schema), schema.equipment())
        }
        schema.registerMob(this, "minecraft:zombie_pigman")
        schema.registerMob(this, "minecraft:zombie_villager")
    }

    override fun registerBlockEntities(schema: Schema) = mutableMapOf<String, Supplier<TypeTemplate>>().apply {
        schema.registerContainer(this, "minecraft:furnace")
        schema.registerContainer(this, "minecraft:chest")
        schema.registerContainer(this, "minecraft:trapped_chest")
        schema.registerSimple(this, "minecraft:ender_chest")
        schema.register(this, "minecraft:jukebox") { _ -> optionalFields("RecordItem", References.ITEM_STACK.`in`(schema)) }
        schema.registerContainer(this, "minecraft:dispenser")
        schema.registerContainer(this, "minecraft:dropper")
        schema.registerSimple(this, "minecraft:sign")
        schema.register(this, "minecraft:mob_spawner") { _ -> References.UNTAGGED_SPAWNER.`in`(schema) }
        schema.register(this, "minecraft:piston") { _ -> optionalFields("blockState", References.BLOCK_STATE.`in`(schema)) }
        schema.registerContainer(this, "minecraft:brewing_stand")
        schema.registerSimple(this, "minecraft:enchanting_table")
        schema.registerSimple(this, "minecraft:end_portal")
        schema.registerSimple(this, "minecraft:beacon")
        schema.registerSimple(this, "minecraft:skull")
        schema.registerSimple(this, "minecraft:daylight_detector")
        schema.registerContainer(this, "minecraft:hopper")
        schema.registerSimple(this, "minecraft:comparator")
        schema.registerSimple(this, "minecraft:banner")
        schema.registerSimple(this, "minecraft:structure_block")
        schema.registerSimple(this, "minecraft:end_gateway")
        schema.registerSimple(this, "minecraft:command_block")
        schema.registerContainer(this, "minecraft:shulker_box")
        schema.registerSimple(this, "minecraft:bed")
    }

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        registerType(false, References.LEVEL, DSL::remainder)
        registerType(false, References.RECIPE) { constType(NAMESPACED_STRING) }
        registerType(false, References.CHUNK) {
            fields(
                "Level", optionalFields(
                    "Entities", list(References.ENTITY_TREE.`in`(this)),
                    "TileEntities", list(References.BLOCK_ENTITY.`in`(this)),
                    "TileTicks", list(fields("i", References.BLOCK_NAME.`in`(this)))
                ),
                "Sections", list(optionalFields("Palette", list(References.BLOCK_STATE.`in`(this))))
            )
        }
        val itemNameId: () -> TypeTemplate = { compoundList(References.ITEM_NAME.`in`(this), constType(intType())) }
        registerType(false, References.STATS) {
            optionalFields("stats", optionalFields(
                "minecraft:mined", compoundList(References.BLOCK_NAME.`in`(this)),
                "minecraft:crafted", itemNameId(),
                "minecraft:used", itemNameId(),
                "minecraft:broken", itemNameId(),
                "minecraft:picked_up", itemNameId(),
                optionalFields(
                    "minecraft:dropped", itemNameId(),
                    "minecraft:killed", compoundList(References.ENTITY_NAME.`in`(this), constType(intType())),
                    "minecraft:killed_by", compoundList(References.ENTITY_NAME.`in`(this), constType(intType())),
                    "minecraft:custom", compoundList(constType(NAMESPACED_STRING), constType(intType()))
                )
            ))
        }
        val criterionTypes = createCriterionTypes()
        registerType(false, References.OBJECTIVE) {
            hook(optionalFields("CriteriaType", taggedChoiceLazy("type", string(), criterionTypes)), V1451S6.UNPACK_OBJECTIVE_ID, V1451S6.REPACK_OBJECTIVE_ID)
        }
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
        registerType(false, References.PLAYER) {
            optionalFields(
                "RootVehicle", optionalFields("Entity", References.ENTITY_TREE.`in`(this)),
                "Inventory", list(References.ITEM_STACK.`in`(this)),
                "EnderItems", list(References.ITEM_STACK.`in`(this)),
                optionalFields(
                    "ShoulderEntityLeft", References.ENTITY_TREE.`in`(this),
                    "ShoulderEntityRight", References.ENTITY_TREE.`in`(this),
                    "recipeBook", optionalFields(
                        "recipes", list(References.RECIPE.`in`(this)),
                        "toBeDisplayed", list(References.RECIPE.`in`(this))
                    )
                )
            )
        }
        registerType(true, References.ENTITY) { taggedChoiceLazy("id", NAMESPACED_STRING, entityTypes) }
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
            ), V705.ADD_NAMES, Hook.HookFunction.IDENTITY)
        }
        registerType(false, References.STRUCTURE) {
            optionalFields(
                "entities", list(optionalFields("nbt", References.ENTITY_TREE.`in`(this))),
                "blocks", list(optionalFields("nbt", References.BLOCK_ENTITY.`in`(this))),
                "palette", list(References.BLOCK_STATE.`in`(this))
            )
        }
        registerType(false, References.BLOCK_STATE, DSL::remainder)
        registerType(true, References.UNTAGGED_SPAWNER) {
            optionalFields(
                "SpawnPotentials", list(fields("Entity", References.ENTITY_TREE.`in`(this))),
                "SpawnData", References.ENTITY_TREE.`in`(this)
            )
        }
        registerType(true, References.BLOCK_ENTITY) { taggedChoiceLazy("id", NAMESPACED_STRING, blockEntityTypes) }
        registerType(true, References.ENTITY_TREE) { optionalFields("Passengers", list(References.ENTITY_TREE.`in`(this)), References.ENTITY.`in`(this)) }
        registerType(false, References.HOTBAR) { compoundList(list(References.ITEM_STACK.`in`(this))) }
        registerType(false, References.BLOCK_NAME) { constType(NAMESPACED_STRING) }
        registerType(false, References.ITEM_NAME) { constType(NAMESPACED_STRING) }
        registerType(false, References.SAVED_DATA) {
            optionalFields("data", optionalFields(
                "Features", compoundList(References.STRUCTURE_FEATURE.`in`(this)),
                "Objectives", list(References.OBJECTIVE.`in`(this)),
                "Teams", list(References.TEAM.`in`(this))
            ))
        }
        registerType(false, References.STRUCTURE_FEATURE) {
            optionalFields("Children", list(optionalFields(
                "CA", References.BLOCK_STATE.`in`(this),
                "CB", References.BLOCK_STATE.`in`(this),
                "CC", References.BLOCK_STATE.`in`(this),
                "CD", References.BLOCK_STATE.`in`(this)
            )))
        }
        registerType(false, References.TEAM, DSL::remainder)
        registerType(false, References.POI_CHUNK, DSL::remainder)
        registerType(true, References.WORLD_GEN_SETTINGS, DSL::remainder)
        registerType(false, References.ENTITY_CHUNK) { optionalFields("Entities", list(References.ENTITY_TREE.`in`(this))) }
    }
}

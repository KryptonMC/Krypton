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
import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.DSL.fields
import com.mojang.datafixers.DSL.hook
import com.mojang.datafixers.DSL.intType
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.DSL.or
import com.mojang.datafixers.DSL.string
import com.mojang.datafixers.DSL.taggedChoiceLazy
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.Hook
import com.mojang.datafixers.types.templates.TypeTemplate
import com.mojang.serialization.Dynamic
import com.mojang.serialization.DynamicOps
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class InitialSchema(versionKey: Int, parent: Schema?) : Schema(versionKey, parent) {

    override fun registerEntities(schema: Schema) = mutableMapOf<String, Supplier<TypeTemplate>>().apply {
        schema.registerItem(this, "Item")
        schema.registerSimple(this, "XPOrb")
        schema.registerProjectile(this, "ThrownEgg")
        schema.registerSimple(this, "LeashKnot")
        schema.registerSimple(this, "Painting")
        schema.registerProjectile(this, "Arrow")
        schema.registerProjectile(this, "TippedArrow")
        schema.registerProjectile(this, "SpectralArrow")
        schema.registerProjectile(this, "Snowball")
        schema.registerProjectile(this, "Fireball")
        schema.registerProjectile(this, "SmallFireball")
        schema.registerProjectile(this, "ThrownEnderpearl")
        schema.registerSimple(this, "EyeOfEnderSignal")
        schema.register(this, "ThrownPotion") { _ ->
            optionalFields("inTile", References.BLOCK_NAME.`in`(schema), "Potion", References.ITEM_STACK.`in`(schema))
        }
        schema.registerProjectile(this, "ThrownExpBottle")
        schema.registerItem(this, "ItemFrame")
        schema.registerProjectile(this, "WitherSkull")
        schema.registerSimple(this, "PrimedTnt")
        schema.register(this, "FallingSand") { _ ->
            optionalFields("Block", References.BLOCK_NAME.`in`(schema), "TileEntityData", References.BLOCK_ENTITY.`in`(schema))
        }
        schema.register(this, "FireworksRocketEntity") { _ -> optionalFields("FireworksItem", References.ITEM_STACK.`in`(schema)) }
        schema.registerSimple(this, "Boat")
        schema.register(this, "Minecart") { _ ->
            optionalFields("DisplayTile", References.BLOCK_NAME.`in`(schema), "Items", list(References.ITEM_STACK.`in`(schema)))
        }
        schema.registerMinecart(this, "MinecartRideable")
        schema.register(this, "MinecartChest") { _ ->
            optionalFields("DisplayTile", References.BLOCK_NAME.`in`(schema), "Items", list(References.ITEM_STACK.`in`(schema)))
        }
        schema.registerMinecart(this, "MinecartFurnace")
        schema.registerMinecart(this, "MinecartTNT")
        schema.register(this, "MinecartSpawner") { _ ->
            optionalFields("DisplayTile", References.BLOCK_NAME.`in`(schema), References.UNTAGGED_SPAWNER.`in`(schema))
        }
        schema.register(this, "MinecartHopper") { _ ->
            optionalFields("DisplayTile", References.BLOCK_NAME.`in`(schema), "Items", list(References.ITEM_STACK.`in`(schema)))
        }
        schema.registerMinecart(this, "MinecartCommandBlock")
        schema.registerMob(this, "ArmorStand")
        schema.registerMob(this, "Creeper")
        schema.registerMob(this, "Skeleton")
        schema.registerMob(this, "Spider")
        schema.registerMob(this, "Giant")
        schema.registerMob(this, "Zombie")
        schema.registerMob(this, "Slime")
        schema.registerMob(this, "Ghast")
        schema.registerMob(this, "PigZombie")
        schema.register(this, "Enderman") { _ -> optionalFields("carried", References.BLOCK_NAME.`in`(schema), schema.equipment()) }
        schema.registerMob(this, "CaveSpider")
        schema.registerMob(this, "Silverfish")
        schema.registerMob(this, "Blaze")
        schema.registerMob(this, "LavaSlime")
        schema.registerMob(this, "EnderDragon")
        schema.registerMob(this, "WitherBoss")
        schema.registerMob(this, "Bat")
        schema.registerMob(this, "Witch")
        schema.registerMob(this, "Endermite")
        schema.registerMob(this, "Guardian")
        schema.registerMob(this, "Pig")
        schema.registerMob(this, "Sheep")
        schema.registerMob(this, "Cow")
        schema.registerMob(this, "Chicken")
        schema.registerMob(this, "Squid")
        schema.registerMob(this, "Wolf")
        schema.registerMob(this, "MushroomCow")
        schema.registerMob(this, "SnowMan")
        schema.registerMob(this, "Ozelot")
        schema.registerMob(this, "VillagerGolem")
        schema.register(this, "EntityHorse") { _ ->
            optionalFields(
                "Items", list(References.ITEM_STACK.`in`(schema)),
                "ArmorItems", References.ITEM_STACK.`in`(schema),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
        schema.registerMob(this, "Rabbit")
        schema.register(this, "Villager") { _ ->
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
        schema.registerSimple(this, "EnderCrystal")
        schema.registerSimple(this, "AreaEffectCloud")
        schema.registerSimple(this, "ShulkerBullet")
        schema.registerMob(this, "Shulker")
    }

    override fun registerBlockEntities(schema: Schema) = mutableMapOf<String, Supplier<TypeTemplate>>().apply {
        schema.registerContainer(this, "Furnace")
        schema.registerContainer(this, "Chest")
        schema.registerSimple(this, "EnderChest")
        schema.register(this, "RecordPlayer") { _ -> optionalFields("RecordItem", References.ITEM_STACK.`in`(schema)) }
        schema.registerContainer(this, "Trap")
        schema.registerContainer(this, "Dropper")
        schema.registerSimple(this, "Sign")
        schema.register(this, "MobSpawner") { _ -> References.UNTAGGED_SPAWNER.`in`(schema) }
        schema.registerSimple(this, "Music")
        schema.registerSimple(this, "Piston")
        schema.registerContainer(this, "Cauldron")
        schema.registerSimple(this, "EnchantTable")
        schema.registerSimple(this, "Airportal")
        schema.registerSimple(this, "Control")
        schema.registerSimple(this, "Beacon")
        schema.registerSimple(this, "Skull")
        schema.registerSimple(this, "DLDetector")
        schema.registerContainer(this, "Hopper")
        schema.registerSimple(this, "Comparator")
        schema.register(this, "FlowerPot") { _ -> optionalFields("Item", or(constType(intType()), References.ITEM_NAME.`in`(schema))) }
        schema.registerSimple(this, "Banner")
        schema.registerSimple(this, "Structure")
        schema.registerSimple(this, "EndGateway")
    }

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        registerType(false, References.LEVEL, DSL::remainder)
        registerType(false, References.PLAYER) { optionalFields("Inventory", list(References.ITEM_STACK.`in`(this))) }
        registerType(false, References.CHUNK) {
            fields("Level", optionalFields(
                "Entities", list(References.ENTITY_TREE.`in`(this)),
                "TileEntities", list(References.BLOCK_ENTITY.`in`(this)),
                "TileTicks", list(fields("i", References.BLOCK_NAME.`in`(this)))
            ))
        }
        registerType(true, References.BLOCK_ENTITY) { taggedChoiceLazy("id", string(), blockEntityTypes) }
        registerType(true, References.ENTITY_TREE) {
            optionalFields("Riding", References.ENTITY_TREE.`in`(this), References.ENTITY.`in`(this))
        }
        registerType(false, References.ENTITY_NAME) { constType(NamespacedSchema.NAMESPACED_STRING) }
        registerType(true, References.ENTITY) { taggedChoiceLazy("id", string(), entityTypes) }
        registerType(true, References.ITEM_STACK) {
            hook(optionalFields(
                "id", or(constType(intType()), References.ITEM_NAME.`in`(this)),
                "tag", optionalFields(
                    "EntityTag", References.ENTITY_TREE.`in`(this),
                    "BlockEntity", References.BLOCK_ENTITY.`in`(this),
                    "CanDestroy", list(References.BLOCK_NAME.`in`(this)),
                    "CanPlaceOn", list(References.BLOCK_NAME.`in`(this)),
                    "Items", list(References.ITEM_STACK.`in`(this))
                )
            ), ADD_NAMES, Hook.HookFunction.IDENTITY)
        }
        registerType(false, References.BLOCK_NAME) { or(constType(intType()), constType(NamespacedSchema.NAMESPACED_STRING)) }
        registerType(false, References.ITEM_NAME) { constType(NamespacedSchema.NAMESPACED_STRING) }
        registerType(false, References.STATS, DSL::remainder)
        registerType(false, References.SAVED_DATA) {
            optionalFields("data", optionalFields(
                "Features", DSL.compoundList(References.STRUCTURE_FEATURE.`in`(this)),
                "Objectives", list(References.OBJECTIVE.`in`(this)),
                "Teams", list(References.TEAM.`in`(this))
            ))
        }
        registerType(false, References.STRUCTURE_FEATURE, DSL::remainder)
        registerType(false, References.OBJECTIVE, DSL::remainder)
        registerType(false, References.TEAM, DSL::remainder)
        registerType(true, References.UNTAGGED_SPAWNER, DSL::remainder)
        registerType(false, References.POI_CHUNK, DSL::remainder)
        registerType(true, References.WORLD_GEN_SETTINGS, DSL::remainder)
        registerType(false, References.ENTITY_CHUNK) { optionalFields("Entities", list(References.ENTITY_TREE.`in`(this))) }
    }

    companion object {

        private val ITEM_TO_BLOCK_ENTITY = mapOf(
            "minecraft:furnace" to "Furnace",
            "minecraft:lit_furnace" to "Furnace",
            "minecraft:chest" to "Chest",
            "minecraft:trapped_chest" to "Chest",
            "minecraft:ender_chest" to "EnderChest",
            "minecraft:jukebox" to "RecordPlayer",
            "minecraft:dispenser" to "Trap",
            "minecraft:dropper" to "Dropper",
            "minecraft:sign" to "Sign",
            "minecraft:mob_spawner" to "MobSpawner",
            "minecraft:noteblock" to "Music",
            "minecraft:brewing_stand" to "Cauldron",
            "minecraft:enhanting_table" to "EnchantTable",
            "minecraft:command_block" to "CommandBlock",
            "minecraft:beacon" to "Beacon",
            "minecraft:skull" to "Skull",
            "minecraft:daylight_detector" to "DLDetector",
            "minecraft:hopper" to "Hopper",
            "minecraft:banner" to "Banner",
            "minecraft:flower_pot" to "FlowerPot",
            "minecraft:repeating_command_block" to "CommandBlock",
            "minecraft:chain_command_block" to "CommandBlock",
            "minecraft:standing_sign" to "Sign",
            "minecraft:wall_sign" to "Sign",
            "minecraft:piston_head" to "Piston",
            "minecraft:daylight_detector_inverted" to "DLDetector",
            "minecraft:unpowered_comparator" to "Comparator",
            "minecraft:powered_comparator" to "Comparator",
            "minecraft:wall_banner" to "Banner",
            "minecraft:standing_banner" to "Banner",
            "minecraft:structure_block" to "Structure",
            "minecraft:end_portal" to "Airportal",
            "minecraft:end_gateway" to "EndGateway",
            "minecraft:shield" to "Banner"
        )
        val ADD_NAMES = object : Hook.HookFunction {
            override fun <T> apply(ops: DynamicOps<T>, value: T) = Dynamic(ops, value).addNames(ITEM_TO_BLOCK_ENTITY, "ArmorStand")
        }
    }
}

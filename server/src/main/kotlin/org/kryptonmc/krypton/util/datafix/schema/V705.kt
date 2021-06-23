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

import com.mojang.datafixers.DSL.hook
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.DSL.taggedChoiceLazy
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.Hook
import com.mojang.datafixers.types.templates.TypeTemplate
import com.mojang.serialization.Dynamic
import com.mojang.serialization.DynamicOps
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V705(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = mutableMapOf<String, Supplier<TypeTemplate>>().apply {
        schema.registerSimple(this, "minecraft:area_effect_cloud")
        schema.registerMob(this, "minecraft:armor_stand")
        schema.registerProjectile(this, "minecraft:arrow")
        schema.registerMob(this, "minecraft:bat")
        schema.registerMob(this, "minecraft:blaze")
        schema.registerSimple(this, "minecraft:boat")
        schema.registerMob(this, "minecraft:cave_spider")
        schema.register(this, "minecraft:chest_minecart") { _ ->
            optionalFields("DisplayTile", References.BLOCK_NAME.`in`(schema), "Items", list(References.ITEM_STACK.`in`(schema)))
        }
        schema.registerMob(this, "minecraft:chicken")
        schema.register(this, "minecraft:commandblock_minecart") { _ -> optionalFields("DisplayTile", References.BLOCK_NAME.`in`(schema)) }
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
        schema.registerProjectile(this, "minecraft:egg")
        schema.registerMob(this, "minecraft:elder_guardian")
        schema.registerSimple(this, "minecraft:ender_crystal")
        schema.registerMob(this, "minecraft:ender_dragon")
        schema.register(this, "minecraft:enderman") { _ ->
            optionalFields("carried", References.BLOCK_NAME.`in`(schema), schema.equipment())
        }
        schema.registerMob(this, "minecraft:endermite")
        schema.registerProjectile(this, "minecraft:ender_pearl")
        schema.registerSimple(this, "minecraft:eye_of_ender_signal")
        schema.register(this, "minecraft:falling_block") { _ ->
            optionalFields(
                "Block", References.BLOCK_NAME.`in`(schema),
                "TileEntityData", References.BLOCK_ENTITY.`in`(schema)
            )
        }
        schema.registerProjectile(this, "minecraft:fireball")
        schema.register(this, "minecraft:fireworks_rocket") { _ ->
            optionalFields("FireworksItem", References.ITEM_STACK.`in`(schema))
        }
        schema.register(this, "minecraft:furnace_minecart") { _ ->
            optionalFields("DisplayTile", References.BLOCK_NAME.`in`(schema))
        }
        schema.registerMob(this, "minecraft:ghast")
        schema.registerMob(this, "minecraft:giant")
        schema.registerMob(this, "minecraft:guardian")
        schema.register(this, "minecraft:hopper_minecart") { _ ->
            optionalFields(
                "DisplayTile", References.BLOCK_NAME.`in`(schema),
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
        schema.register(this, "minecraft:item") { _ -> optionalFields("Item", References.ITEM_STACK.`in`(schema)) }
        schema.register(this, "minecraft:item_frame") { _ -> optionalFields("Item", References.ITEM_STACK.`in`(schema)) }
        schema.registerSimple(this, "minecraft:leash_knot")
        schema.registerMob(this, "minecraft:magma_cube")
        schema.register(this, "minecraft:minecart") { _ -> optionalFields("DisplayTile", References.BLOCK_NAME.`in`(schema)) }
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
        schema.register(this, "minecraft:potion") { _ ->
            optionalFields(
                "Potion", References.ITEM_STACK.`in`(schema),
                "inTile", References.BLOCK_NAME.`in`(schema)
            )
        }
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
        schema.registerProjectile(this, "minecraft:small_fireball")
        schema.registerProjectile(this, "minecraft:snowball")
        schema.registerMob(this, "minecraft:snowman")
        schema.register(this, "minecraft:spawner_minecart") { _ ->
            optionalFields("DisplayTile", References.BLOCK_NAME.`in`(schema), References.UNTAGGED_SPAWNER.`in`(schema))
        }
        schema.register(this, "minecraft:spectral_arrow") { _ -> optionalFields("inTile", References.BLOCK_NAME.`in`(schema)) }
        schema.registerMob(this, "minecraft:spider")
        schema.registerMob(this, "minecraft:squid")
        schema.registerMob(this, "minecraft:stray")
        schema.registerSimple(this, "minecraft:tnt")
        schema.register(this, "minecraft:tnt_minecart") { _ -> optionalFields("DisplayTile", References.BLOCK_NAME.`in`(schema)) }
        schema.register(this, "minecraft:villager") { _ ->
            optionalFields(
                "Inventory", list(References.ITEM_STACK.`in`(schema)),
                "Offers", optionalFields(
                    "Recipes", list(
                        optionalFields(
                        "buy", References.ITEM_STACK.`in`(schema),
                        "buyB", References.ITEM_STACK.`in`(schema),
                        "sell", References.ITEM_STACK.`in`(schema))
                    )
                ), schema.equipment()
            )
        }
        schema.registerMob(this, "minecraft:villager_golem")
        schema.registerMob(this, "minecraft:witch")
        schema.registerMob(this, "minecraft:wither")
        schema.registerMob(this, "minecraft:wither_skeleton")
        schema.registerProjectile(this, "minecraft:wither_skull")
        schema.registerMob(this, "minecraft:wolf")
        schema.registerProjectile(this, "minecraft:xp_bottle")
        schema.registerSimple(this, "minecraft:xp_orb")
        schema.registerMob(this, "minecraft:zombie")
        schema.register(this, "minecraft:zombie_horse") { _ ->
            optionalFields("SaddleItem", References.ITEM_STACK.`in`(schema), schema.equipment())
        }
        schema.registerMob(this, "minecraft:zombie_pigman")
        schema.registerMob(this, "minecraft:zombie_villager")
        schema.registerSimple(this, "minecraft:evocation_fangs")
        schema.registerMob(this, "minecraft:evocation_illager")
        schema.registerSimple(this, "minecraft:illusion_illager")
        schema.register(this, "minecraft:llama") { _ ->
            optionalFields(
                "Items", list(References.ITEM_STACK.`in`(schema)),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                "DecorItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
        schema.registerSimple(this, "minecraft:llama_spit")
        schema.registerMob(this, "minecraft:vex")
        schema.registerMob(this, "minecraft:vindication_illager")
    }

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
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
            ), ADD_NAMES, Hook.HookFunction.IDENTITY)
        }
    }

    companion object {

        val ADD_NAMES = object : Hook.HookFunction {
            override fun <T> apply(ops: DynamicOps<T>, value: T) = Dynamic(ops, value).addNames(V704.ITEM_TO_BLOCK_ENTITY, "minecraft:armor_stand")
        }
    }
}

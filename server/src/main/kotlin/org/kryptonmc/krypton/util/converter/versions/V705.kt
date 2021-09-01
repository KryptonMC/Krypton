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
package org.kryptonmc.krypton.util.converter.versions

import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.hooks.EnforceNamespacedDataHook
import org.kryptonmc.krypton.util.converter.hooks.EnforceNamespacedValueTypeDataHook
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.walkers.BlockNamesDataWalker
import org.kryptonmc.krypton.util.converter.walkers.ItemListsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.ItemsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.TileEntitiesDataWalker
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converter.walkers.convertList
import org.kryptonmc.krypton.util.converters.RenameEntitiesConverter
import org.kryptonmc.krypton.util.logger

object V705 {

    private val LOGGER = logger<V705>()
    private const val VERSION = MCVersions.V1_10_2 + 193
    private val ENTITY_ID_UPDATE = mapOf(
        "AreaEffectCloud" to "minecraft:area_effect_cloud",
        "ArmorStand" to "minecraft:armor_stand",
        "Arrow" to "minecraft:arrow",
        "Bat" to "minecraft:bat",
        "Blaze" to "minecraft:blaze",
        "Boat" to "minecraft:boat",
        "CaveSpider" to "minecraft:cave_spider",
        "Chicken" to "minecraft:chicken",
        "Cow" to "minecraft:cow",
        "Creeper" to "minecraft:creeper",
        "Donkey" to "minecraft:donkey",
        "DragonFireball" to "minecraft:dragon_fireball",
        "ElderGuardian" to "minecraft:elder_guardian",
        "EnderCrystal" to "minecraft:ender_crystal",
        "EnderDragon" to "minecraft:ender_dragon",
        "Enderman" to "minecraft:enderman",
        "Endermite" to "minecraft:endermite",
        "EyeOfEnderSignal" to "minecraft:eye_of_ender_signal",
        "FallingSand" to "minecraft:falling_block",
        "Fireball" to "minecraft:fireball",
        "FireworksRocketEntity" to "minecraft:fireworks_rocket",
        "Ghast" to "minecraft:ghast",
        "Giant" to "minecraft:giant",
        "Guardian" to "minecraft:guardian",
        "Horse" to "minecraft:horse",
        "Husk" to "minecraft:husk",
        "Item" to "minecraft:item",
        "ItemFrame" to "minecraft:item_frame",
        "LavaSlime" to "minecraft:magma_cube",
        "LeashKnot" to "minecraft:leash_knot",
        "MinecartChest" to "minecraft:chest_minecart",
        "MinecartCommandBlock" to "minecraft:commandblock_minecart",
        "MinecartFurnace" to "minecraft:furnace_minecart",
        "MinecartHopper" to "minecraft:hopper_minecart",
        "MinecartRideable" to "minecraft:minecart",
        "MinecartSpawner" to "minecraft:spawner_minecart",
        "MinecartTNT" to "minecraft:tnt_minecart",
        "Mule" to "minecraft:mule",
        "MushroomCow" to "minecraft:mooshroom",
        "Ozelot" to "minecraft:ocelot",
        "Painting" to "minecraft:painting",
        "Pig" to "minecraft:pig",
        "PigZombie" to "minecraft:zombie_pigman",
        "PolarBear" to "minecraft:polar_bear",
        "PrimedTnt" to "minecraft:tnt",
        "Rabbit" to "minecraft:rabbit",
        "Sheep" to "minecraft:sheep",
        "Shulker" to "minecraft:shulker",
        "ShulkerBullet" to "minecraft:shulker_bullet",
        "Silverfish" to "minecraft:silverfish",
        "Skeleton" to "minecraft:skeleton",
        "SkeletonHorse" to "minecraft:skeleton_horse",
        "Slime" to "minecraft:slime",
        "SmallFireball" to "minecraft:small_fireball",
        "SnowMan" to "minecraft:snowman",
        "Snowball" to "minecraft:snowball",
        "SpectralArrow" to "minecraft:spectral_arrow",
        "Spider" to "minecraft:spider",
        "Squid" to "minecraft:squid",
        "Stray" to "minecraft:stray",
        "ThrownEgg" to "minecraft:egg",
        "ThrownEnderpearl" to "minecraft:ender_pearl",
        "ThrownExpBottle" to "minecraft:xp_bottle",
        "ThrownPotion" to "minecraft:potion",
        "Villager" to "minecraft:villager",
        "VillagerGolem" to "minecraft:villager_golem",
        "Witch" to "minecraft:witch",
        "WitherBoss" to "minecraft:wither",
        "WitherSkeleton" to "minecraft:wither_skeleton",
        "WitherSkull" to "minecraft:wither_skull",
        "Wolf" to "minecraft:wolf",
        "XPOrb" to "minecraft:xp_orb",
        "Zombie" to "minecraft:zombie",
        "ZombieHorse" to "minecraft:zombie_horse",
        "ZombieVillager" to "minecraft:zombie_villager",
    )

    fun register() {
        RenameEntitiesConverter.register(VERSION, ENTITY_ID_UPDATE::get)

        registerMob("minecraft:armor_stand")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:arrow", BlockNamesDataWalker("inTile"))
        registerMob("minecraft:bat")
        registerMob("minecraft:blaze")
        registerMob("minecraft:cave_spider")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:chest_minecart", BlockNamesDataWalker("DisplayTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:chest_minecart", ItemListsDataWalker("Items"))
        registerMob("minecraft:chicken")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:commandblock_minecart", BlockNamesDataWalker("DisplayTile"))
        registerMob("minecraft:cow")
        registerMob("minecraft:creeper")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:donkey", ItemListsDataWalker("Items", "ArmorItems", "HandItems"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:donkey", ItemsDataWalker("SaddleItem"))
        registerThrowableProjectile("minecraft:egg")
        registerMob("minecraft:elder_guardian")
        registerMob("minecraft:ender_dragon")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:enderman", ItemListsDataWalker("ArmorItems", "HandItems"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:enderman", BlockNamesDataWalker("carried"))
        registerMob("minecraft:endermite")
        registerThrowableProjectile("minecraft:ender_pearl")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:falling_block", BlockNamesDataWalker("Block"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:falling_block", TileEntitiesDataWalker("TileEntityData"))
        registerThrowableProjectile("minecraft:fireball")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:fireworks_rocket", ItemsDataWalker("FireworksItem"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:furnace_minecart", BlockNamesDataWalker("DisplayTile"))
        registerMob("minecraft:ghast")
        registerMob("minecraft:giant")
        registerMob("minecraft:guardian")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:hopper_minecart", BlockNamesDataWalker("DisplayTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:hopper_minecart", ItemListsDataWalker("Items"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:horse", ItemsDataWalker("ArmorItem", "SaddleItem"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:horse", ItemListsDataWalker("ArmorItems", "HandItems"))
        registerMob("minecraft:husk")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:item", ItemsDataWalker("Item"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:item_frame", ItemsDataWalker("Item"))
        registerMob("minecraft:magma_cube")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:minecart", BlockNamesDataWalker("DisplayTile"))
        registerMob("minecraft:mooshroom")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:mule", ItemListsDataWalker("Items", "ArmorItems", "HandItems"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:mule", ItemsDataWalker("SaddleItem"))
        registerMob("minecraft:ocelot")
        registerMob("minecraft:pig")
        registerMob("minecraft:polar_bear")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:potion", ItemsDataWalker("Potion"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:potion", BlockNamesDataWalker("inTile"))
        registerMob("minecraft:rabbit")
        registerMob("minecraft:sheep")
        registerMob("minecraft:shulker")
        registerMob("minecraft:silverfish")
        registerMob("minecraft:skeleton")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:skeleton_horse", ItemListsDataWalker("ArmorItems", "HandItems"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:skeleton_horse", ItemsDataWalker("SaddleItem"))
        registerMob("minecraft:slime")
        registerThrowableProjectile("minecraft:small_fireball")
        registerThrowableProjectile("minecraft:snowball")
        registerMob("minecraft:snowman")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:spawner_minecart", BlockNamesDataWalker("DisplayTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:spawner_minecart") { data, fromVersion, toVersion ->
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion)
            null
        }
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:spectral_arrow", BlockNamesDataWalker("inTile"))
        registerMob("minecraft:spider")
        registerMob("minecraft:squid")
        registerMob("minecraft:stray")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:tnt_minecart", BlockNamesDataWalker("DisplayTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:villager") { data, fromVersion, toVersion ->
            data.convertList(MCTypeRegistry.ITEM_STACK, "Inventory", fromVersion, toVersion)

            data.getMap<String>("offers")?.let { offers ->
                offers.getList("Recipes", ObjectType.MAP)?.let {
                    for (i in 0 until it.size()) {
                        val recipe = it.getMap<String>(i)
                        recipe.convert(MCTypeRegistry.ITEM_STACK, "buy", fromVersion, toVersion)
                        recipe.convert(MCTypeRegistry.ITEM_STACK, "buyB", fromVersion, toVersion)
                        recipe.convert(MCTypeRegistry.ITEM_STACK, "sell", fromVersion, toVersion)
                    }
                }
            }

            data.convertList(MCTypeRegistry.ITEM_STACK, "ArmorItems", fromVersion, toVersion)
            data.convertList(MCTypeRegistry.ITEM_STACK, "HandItems", fromVersion, toVersion)
            null
        }
        registerMob("minecraft:villager_golem")
        registerMob("minecraft:witch")
        registerMob("minecraft:wither")
        registerMob("minecraft:wither_skeleton")
        registerThrowableProjectile("minecraft:wither_skull")
        registerMob("minecraft:wolf")
        registerThrowableProjectile("minecraft:xp_bottle")
        registerMob("minecraft:zombie")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:zombie_horse", ItemsDataWalker("SaddleItem"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:zombie_horse", ItemListsDataWalker("ArmorItems", "HandItems"))
        registerMob("minecraft:zombie_pigman")
        registerMob("minecraft:zombie_villager")
        registerMob("minecraft:evocation_illager")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:llama", ItemListsDataWalker("Items", "ArmorItems", "HandItems"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:llama", ItemsDataWalker("SaddleItem", "DecorItem"))
        registerMob("minecraft:vex")
        registerMob("minecraft:vindication_illager")
        // Don't need to re-register itemstack walker, the V704 will correctly choose the right id for armorstand based on
        // the source version

        // Enforce namespace for ids
        MCTypeRegistry.ENTITY.addStructureHook(VERSION, EnforceNamespacedDataHook())
        MCTypeRegistry.ENTITY_NAME.addStructureHook(VERSION, EnforceNamespacedValueTypeDataHook())
    }

    private fun registerMob(id: String) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, ItemListsDataWalker("ArmorItems", "HandItems"))
    }

    private fun registerThrowableProjectile(id: String) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, BlockNamesDataWalker("inTile"))
    }
}

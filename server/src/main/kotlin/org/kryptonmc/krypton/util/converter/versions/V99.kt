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
import org.kryptonmc.krypton.util.converters.helpers.ItemNameHelper
import org.kryptonmc.krypton.util.converter.hooks.EnforceNamespacedDataHook
import org.kryptonmc.krypton.util.converter.hooks.EnforceNamespacedValueTypeDataHook
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.walkers.BlockNamesDataWalker
import org.kryptonmc.krypton.util.converter.walkers.ItemListsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.ItemNamesDataWalker
import org.kryptonmc.krypton.util.converter.walkers.ItemsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.TileEntitiesDataWalker
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converter.walkers.convertList
import org.kryptonmc.krypton.util.converter.walkers.convertValues
import org.kryptonmc.krypton.util.logger

object V99 {

    private val LOGGER = logger<V99>()
    private const val VERSION = MCVersions.V15W32A - 1
    private val ITEM_ID_TO_TILE_ENTITY_ID = mapOf(
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

    fun register() {
        MCTypeRegistry.ENTITY.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            data.convert(MCTypeRegistry.ENTITY, "Riding", fromVersion, toVersion)
            null
        }
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Item", ItemsDataWalker("Item"))
        registerProjectile("ThrownEgg")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Arrow", BlockNamesDataWalker("inTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "TippedArrow", BlockNamesDataWalker("inTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "SpectralArrow", BlockNamesDataWalker("inTile"))
        registerProjectile("Snowball")
        registerProjectile("Fireball")
        registerProjectile("SmallFireball")
        registerProjectile("ThrownEnderpearl")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "ThrownPotion", BlockNamesDataWalker("inTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "ThrownPotion", ItemsDataWalker("Potion"))
        registerProjectile("ThrownExpBottle")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "ItemFrame", ItemsDataWalker("Item"))
        registerProjectile("WitherSkull")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "FallingSand", BlockNamesDataWalker("Block"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "FallingSand", TileEntitiesDataWalker("TileEntityData"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "FireworksRocketEntity", ItemsDataWalker("FireworksItem"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Minecart", BlockNamesDataWalker("DisplayTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Minecart", ItemListsDataWalker("Items"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Minecart") { data, fromVersion, toVersion ->
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion)
            null
        }
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartChest", BlockNamesDataWalker("DisplayTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartChest", ItemListsDataWalker("Items"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartFurnace", BlockNamesDataWalker("DisplayTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartTNT", BlockNamesDataWalker("DisplayTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartSpawner", BlockNamesDataWalker("DisplayTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartSpawner") { data, fromVersion, toVersion ->
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion)
            null
        }
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartHopper", BlockNamesDataWalker("DisplayTile"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartHopper", ItemListsDataWalker("Items"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartCommandBlock", BlockNamesDataWalker("DisplayTile"))
        registerMob("ArmorStand")
        registerMob("Creeper")
        registerMob("Skeleton")
        registerMob("Spider")
        registerMob("Giant")
        registerMob("Zombie")
        registerMob("Slime")
        registerMob("Ghast")
        registerMob("PigZombie")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Enderman", BlockNamesDataWalker("carried"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Enderman", ItemListsDataWalker("Equipment"))
        registerMob("CaveSpider")
        registerMob("Silverfish")
        registerMob("Blaze")
        registerMob("LavaSlime")
        registerMob("EnderDragon")
        registerMob("WitherBoss")
        registerMob("Bat")
        registerMob("Witch")
        registerMob("Endermite")
        registerMob("Guardian")
        registerMob("Pig")
        registerMob("Sheep")
        registerMob("Cow")
        registerMob("Chicken")
        registerMob("Squid")
        registerMob("Wolf")
        registerMob("MushroomCow")
        registerMob("SnowMan")
        registerMob("Ozelot")
        registerMob("VillagerGolem")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "EntityHorse", ItemListsDataWalker("Items", "Equipment"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "EntityHorse", ItemsDataWalker("ArmorItem", "SaddleItem"))
        registerMob("Rabbit")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Villager", ItemListsDataWalker("Inventory", "Equipment"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Villager") { data, fromVersion, toVersion ->
            val offers = data.getMap<String>("Offers") ?: return@addWalker null
            val recipes = offers.getList("Recipes", ObjectType.MAP) ?: return@addWalker null
            for (i in 0 until recipes.size()) {
                val recipe = recipes.getMap<String>(i)
                recipe.convert(MCTypeRegistry.ITEM_STACK, "buy", fromVersion, toVersion)
                recipe.convert(MCTypeRegistry.ITEM_STACK, "buyB", fromVersion, toVersion)
                recipe.convert(MCTypeRegistry.ITEM_STACK, "sell", fromVersion, toVersion)
            }
            null
        }
        registerMob("Shulker")

        registerInventory("Furnace")
        registerInventory("Chest")
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "RecordPlayer", ItemsDataWalker("RecordItem"))
        registerInventory("Trap")
        registerInventory("Dropper")
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "MobSpawner") { data, fromVersion, toVersion ->
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion)
            null
        }
        registerInventory("Cauldron")
        registerInventory("Hopper")
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "FlowerPot", ItemNamesDataWalker("Item"))

        MCTypeRegistry.ITEM_STACK.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            data.convert(MCTypeRegistry.ITEM_NAME, "id", fromVersion, toVersion)
            val tag = data.getMap<String>("tag") ?: return@addStructureWalker null

            // only things here are in tag, if changed update if above
            tag.convertList(MCTypeRegistry.ITEM_STACK, "Items", fromVersion, toVersion)
            var entityTag = tag.getMap<String>("EntityTag")
            if (entityTag != null) {
                val itemId = getStringId(data.getString("id")!!)
                val entityId = when (itemId) {
                    "minecraft:armor_stand" -> "ArmorStand"
                    "minecraft:item_frame" -> "ItemFrame"
                    else -> entityTag.getString("id")
                }

                val removeId = if (entityId == null) {
                    if (itemId != "minecraft:air") LOGGER.warn("Unable to resolve Entity for ItemStack: " +
                            "${data.getGeneric("id")} (V99)")
                    false
                } else {
                    entityTag.hasKey("id", ObjectType.STRING).apply {
                        if (this) entityTag!!.setString("id", entityId)
                    }
                }

                val replace = MCTypeRegistry.ENTITY.convert(entityTag, fromVersion, toVersion)
                if (replace != null) {
                    entityTag = replace
                    tag.setMap("EntityTag", entityTag)
                }
                if (removeId) entityTag.remove("id")
            }

            var blockEntityTag = tag.getMap<String>("BlockEntityTag")
            if (blockEntityTag != null) {
                val itemId = getStringId(data.getString("id")!!)
                val entityId = ITEM_ID_TO_TILE_ENTITY_ID[itemId]
                val removeId = if (entityId == null) {
                    if (itemId != "minecraft:air") LOGGER.warn("Unable to resolve Entity for ItemStack: " +
                            "${data.getGeneric("id")} (V99)")
                    false
                } else {
                    !blockEntityTag.hasKey("id", ObjectType.STRING).apply {
                        blockEntityTag!!.setString("id", entityId)
                    }
                }
                val replace = MCTypeRegistry.TILE_ENTITY.convert(blockEntityTag, fromVersion, toVersion)
                if (replace != null) {
                    blockEntityTag = replace
                    tag.setMap("BlockEntityTag", blockEntityTag)
                }
                if (removeId) blockEntityTag.remove("id")
            }

            tag.convertList(MCTypeRegistry.BLOCK_NAME, "CanDestroy", fromVersion, toVersion)
            tag.convertList(MCTypeRegistry.BLOCK_NAME, "CanPlaceOn", fromVersion, toVersion)
            null
        }

        MCTypeRegistry.PLAYER.addStructureWalker(VERSION, ItemListsDataWalker("Inventory", "EnderItems"))

        MCTypeRegistry.CHUNK.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            val level = data.getMap<String>("Level") ?: return@addStructureWalker null
            level.convertList(MCTypeRegistry.ENTITY, "Entities", fromVersion, toVersion)
            level.convertList(MCTypeRegistry.TILE_ENTITY, "TileEntities", fromVersion, toVersion)

            level.getList("TileTicks", ObjectType.MAP)?.let {
                for (i in 0 until it.size()) {
                    val tileTick = it.getMap<String>(i)
                    tileTick.convert(MCTypeRegistry.BLOCK_NAME, "i", fromVersion, toVersion)
                }
            }
            null
        }

        MCTypeRegistry.ENTITY_CHUNK.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            data.convertList(MCTypeRegistry.ENTITY, "Entities", fromVersion, toVersion)
            null
        }

        MCTypeRegistry.SAVED_DATA.addStructureWalker(VERSION) { root, fromVersion, toVersion ->
            val data = root.getMap<String>("data") ?: return@addStructureWalker null
            data.convertValues(MCTypeRegistry.STRUCTURE_FEATURE, "Features", fromVersion, toVersion)
            data.convertList(MCTypeRegistry.OBJECTIVE, "Objectives", fromVersion, toVersion)
            data.convertList(MCTypeRegistry.TEAM, "Teams", fromVersion, toVersion)
            null
        }

        MCTypeRegistry.BLOCK_NAME.addStructureHook(VERSION, EnforceNamespacedValueTypeDataHook())
        MCTypeRegistry.ITEM_NAME.addStructureHook(VERSION, EnforceNamespacedValueTypeDataHook())
        MCTypeRegistry.ITEM_STACK.addStructureHook(VERSION, EnforceNamespacedDataHook())
    }

    private fun registerMob(id: String) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, ItemListsDataWalker("Equipment"))
    }

    private fun registerProjectile(id: String) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, BlockNamesDataWalker("inTile"))
    }

    private fun registerInventory(id: String) {
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, id, ItemListsDataWalker("Items"))
    }

    private fun getStringId(id: Any): String? {
        if (id is String) return id
        if (id is Number) return ItemNameHelper.getNameFromId(id.toInt())
        return null
    }
}

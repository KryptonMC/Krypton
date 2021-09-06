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
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import org.kryptonmc.krypton.util.converter.walkers.BlockNamesDataWalker
import org.kryptonmc.krypton.util.converter.walkers.ItemListsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.ItemsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converter.walkers.convertList
import kotlin.math.min

object V100 {

    private const val VERSION = MCVersions.V15W32A

    fun register() {
        MCTypeRegistry.ENTITY.addStructureConverter(VERSION) { data, _, _ ->
            val equipment = data.getList("Equipment", ObjectType.MAP)
            data.remove("Equipment")

            if (equipment != null) {
                if (equipment.size() > 0 && data.getListUnchecked("HandItems") == null) {
                    val handItems = NBTTypeUtil.createEmptyList()
                    data.setList("HandItems", handItems)
                    handItems.addMap(equipment.getMap<String>(0))
                    handItems.addMap(NBTTypeUtil.createEmptyMap<String>())
                }

                if (equipment.size() > 1 && data.getListUnchecked("ArmorItems") == null) {
                    val armorItems = NBTTypeUtil.createEmptyList()
                    data.setList("ArmorItems", armorItems)
                    for (i in 1 until min(equipment.size(), 5)) {
                        armorItems.addMap(equipment.getMap<String>(i))
                    }
                }
            }

            val dropChances = data.getList("DropChances", ObjectType.FLOAT)
            data.remove("DropChances")

            if (dropChances != null) {
                if (data.getListUnchecked("HandDropChances") == null) {
                    val handDropChances = NBTTypeUtil.createEmptyList()
                    data.setList("HandDropChances", handDropChances)
                    handDropChances.addFloat(if (dropChances.size() > 0) dropChances.getFloat(0) else 0F)
                    handDropChances.addFloat(0F)
                }

                if (data.getListUnchecked("ArmorDropChances") == null) {
                    val armorDropChances = NBTTypeUtil.createEmptyList()
                    data.setList("ArmorDropChances", armorDropChances)
                    for (i in 1..4) {
                        armorDropChances.addFloat(if (dropChances.size() > i) dropChances.getFloat(i) else 0F)
                    }
                }
            }
            null
        }

        registerMob("ArmorStand")
        registerMob("Creeper")
        registerMob("Skeleton")
        registerMob("Spider")
        registerMob("Giant")
        registerMob("Zombie")
        registerMob("Slime")
        registerMob("Ghast")
        registerMob("PigZombie")
        registerMob("Enderman")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Enderman", BlockNamesDataWalker("carried"))
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
        MCTypeRegistry.ENTITY.addWalker(
            VERSION,
            "EntityHorse",
            ItemListsDataWalker("Items", "ArmorItems", "HandItems")
        )
        MCTypeRegistry.ENTITY.addWalker(VERSION, "EntityHorse", ItemsDataWalker("ArmorItem", "SaddleItem"))
        registerMob("Rabbit")
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Villager") { data, fromVersion, toVersion ->
            data.convertList(MCTypeRegistry.ITEM_STACK, "Inventory", fromVersion, toVersion)

            val offers = data.getMap<String>("Offers")
            if (offers != null) {
                val recipes = offers.getList("Recipes", ObjectType.MAP)
                if (recipes != null) {
                    for (i in 0 until recipes.size()) {
                        val recipe = recipes.getMap<String>(i)
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
        registerMob("Shulker")

        MCTypeRegistry.STRUCTURE.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            val entities = data.getList("entities", ObjectType.MAP)
            if (entities != null) {
                for (i in 0 until entities.size()) {
                    entities.getMap<String>(i).convert(MCTypeRegistry.ENTITY, "nbt", fromVersion, toVersion)
                }
            }

            val blocks = data.getList("blocks", ObjectType.MAP)
            if (blocks != null) {
                for (i in 0 until blocks.size()) {
                    blocks.getMap<String>(i).convert(MCTypeRegistry.TILE_ENTITY, "nbt", fromVersion, toVersion)
                }
            }

            data.convertList(MCTypeRegistry.BLOCK_STATE, "palette", fromVersion, toVersion)
            null
        }
    }

    private fun registerMob(id: String) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, ItemListsDataWalker("ArmorItems", "HandItems"))
    }
}

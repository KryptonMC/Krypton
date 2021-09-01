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

import ca.spottedleaf.dataconverter.converters.DataConverter
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.hooks.EnforceNamespacedDataHook
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.walkers.ItemListsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.ItemNamesDataWalker
import org.kryptonmc.krypton.util.converter.walkers.ItemsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converter.walkers.convertList
import org.kryptonmc.krypton.util.logger

object V704 {

    private val LOGGER = logger<V704>()
    private const val VERSION = MCVersions.V1_10_2 + 192
    val ITEM_ID_TO_TILE_ENTITY_ID = mapOf(
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
        "minecraft:sculk_sensor" to "minecraft:sculk_sensor",
        // These are missing from Vanilla (TODO: check on update)
        "minecraft:enchanting_table" to "minecraft:enchanting_table",
        "minecraft:comparator" to "minecraft:comparator",
        "minecraft:light_gray_bed" to "minecraft:bed",
        "minecraft:light_gray_banner" to "minecraft:banner",
        "minecraft:soul_campfire" to "minecraft:campfire",
    )
    private val TILE_ID_UPDATE = mapOf(
        "Airportal" to "minecraft:end_portal",
        "Banner" to "minecraft:banner",
        "Beacon" to "minecraft:beacon",
        "Cauldron" to "minecraft:brewing_stand",
        "Chest" to "minecraft:chest",
        "Comparator" to "minecraft:comparator",
        "Control" to "minecraft:command_block",
        "DLDetector" to "minecraft:daylight_detector",
        "Dropper" to "minecraft:dropper",
        "EnchantTable" to "minecraft:enchanting_table",
        "EndGateway" to "minecraft:end_gateway",
        "EnderChest" to "minecraft:ender_chest",
        "FlowerPot" to "minecraft:flower_pot",
        "Furnace" to "minecraft:furnace",
        "Hopper" to "minecraft:hopper",
        "MobSpawner" to "minecraft:mob_spawner",
        "Music" to "minecraft:noteblock",
        "Piston" to "minecraft:piston",
        "RecordPlayer" to "minecraft:jukebox",
        "Sign" to "minecraft:sign",
        "Skull" to "minecraft:skull",
        "Structure" to "minecraft:structure_block",
        "Trap" to "minecraft:dispenser"
    )

    fun register() {
        MCTypeRegistry.TILE_ENTITY.addStructureConverter(VERSION) { data, _, _ ->
            val id = data.getString("id") ?: return@addStructureConverter null
            data.setString("id", TILE_ID_UPDATE.getOrDefault(id, id))
            null
        }

        registerInventory("minecraft:furnace")
        registerInventory("minecraft:chest")
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:jukebox", ItemsDataWalker("RecordItem"))
        registerInventory("minecraft:dispenser")
        registerInventory("minecraft:dropper")
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:mob_spawner") { data, fromVersion, toVersion ->
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion)
            null
        }
        registerInventory("minecraft:brewing_stand")
        registerInventory("minecraft:hopper")
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:flower_pot", ItemNamesDataWalker("Item"))

        MCTypeRegistry.ITEM_STACK.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            data.convert(MCTypeRegistry.ITEM_NAME, "id", fromVersion, toVersion)
            val tag = data.getMap<String>("tag") ?: return@addStructureWalker null

            // only things here are in tag, if changed update if above
            tag.convertList(MCTypeRegistry.ITEM_STACK, "Items", fromVersion, toVersion)

            var entityTag = tag.getMap<String>("EntityTag")
            if (entityTag != null) {
                val itemId = data.getString("id")
                val entityId = if (itemId == "minecraft:armor_stand") {
                    // The check for version id is changed here. For whatever reason, the legacy
                    // data converters used entity id "minecraft:armor_stand" when version was greater-than 514,
                    // but entity ids were not namespaced until V705! So somebody fucked up the legacy converters.
                    // DFU agrees with my analysis here, it will only set the entityId here to the namespaced variant
                    // with the V705 schema.
                    if (DataConverter.getVersion(fromVersion) < 705) "ArmorStand" else "minecraft:armor_stand"
                } else if (itemId != null && itemId.contains("_spawn_egg")) {
                    // V1451 changes spawn eggs to have the sub entity id be a part of the item id, but of course Mojang never
                    // bothered to write in logic to set the sub entity id, so we have to.
                    // format is ALWAYS <namespace>:<id>_spawn_egg post flattening
                    itemId.substring(0, itemId.indexOf("_spawn_egg"))
                } else if (itemId == "minecraft:item_frame") {
                    // add missing item_frame entity id
                    // version check is same for armorstand, as both were namespaced at the same time
                    if (DataConverter.getVersion(fromVersion) < 705) "ItemFrame" else "minecraft:item_frame"
                } else {
                    entityTag.getString("id")
                }

                val removeId = if (entityId == null) {
                    if (itemId != "minecraft:air") LOGGER.warn("Unable to resolve Entity for ItemStack: $itemId (V704)")
                    false
                } else {
                    !entityTag.hasKey("id", ObjectType.STRING).apply { if (this) entityTag!!.setString("id", entityId) }
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
                val itemId = data.getString("id")
                val entityId = ITEM_ID_TO_TILE_ENTITY_ID[itemId]
                val removeId = if (entityId == null) {
                    if (itemId != "minecraft:air") LOGGER.warn("Unable to resolve BlockEntity for ItemStack: $itemId (V704)")
                    false
                } else {
                    !blockEntityTag.hasKey("id", ObjectType.STRING).apply { if (this) blockEntityTag!!.setString("id", entityId) }
                }
                val replace = MCTypeRegistry.TILE_ENTITY.convert(blockEntityTag, fromVersion, toVersion)
                if (replace != null) {
                    blockEntityTag = replace
                    tag.setMap("BlockEntityTag", entityTag)
                }
                if (removeId) blockEntityTag.remove("id")
            }

            tag.convertList(MCTypeRegistry.BLOCK_NAME, "CanDestroy", fromVersion, toVersion)
            tag.convertList(MCTypeRegistry.BLOCK_NAME, "CanPlaceOn", fromVersion, toVersion)
            null
        }

        // Enforce namespace for ids
        MCTypeRegistry.TILE_ENTITY.addStructureHook(VERSION, EnforceNamespacedDataHook())
    }

    private fun registerInventory(id: String) {
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, id, ItemListsDataWalker("Items"))
    }
}

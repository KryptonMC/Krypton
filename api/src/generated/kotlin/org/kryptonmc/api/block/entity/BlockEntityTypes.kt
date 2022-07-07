/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(BlockEntityType::class)
public object BlockEntityTypes {

    // @formatter:off
    @JvmField
    public val FURNACE: BlockEntityType = get("furnace")
    @JvmField
    public val CHEST: BlockEntityType = get("chest")
    @JvmField
    public val TRAPPED_CHEST: BlockEntityType = get("trapped_chest")
    @JvmField
    public val ENDER_CHEST: BlockEntityType = get("ender_chest")
    @JvmField
    public val JUKEBOX: BlockEntityType = get("jukebox")
    @JvmField
    public val DISPENSER: BlockEntityType = get("dispenser")
    @JvmField
    public val DROPPER: BlockEntityType = get("dropper")
    @JvmField
    public val SIGN: BlockEntityType = get("sign")
    @JvmField
    public val MOB_SPAWNER: BlockEntityType = get("mob_spawner")
    @JvmField
    public val PISTON: BlockEntityType = get("piston")
    @JvmField
    public val BREWING_STAND: BlockEntityType = get("brewing_stand")
    @JvmField
    public val ENCHANTING_TABLE: BlockEntityType = get("enchanting_table")
    @JvmField
    public val END_PORTAL: BlockEntityType = get("end_portal")
    @JvmField
    public val BEACON: BlockEntityType = get("beacon")
    @JvmField
    public val SKULL: BlockEntityType = get("skull")
    @JvmField
    public val DAYLIGHT_DETECTOR: BlockEntityType = get("daylight_detector")
    @JvmField
    public val HOPPER: BlockEntityType = get("hopper")
    @JvmField
    public val COMPARATOR: BlockEntityType = get("comparator")
    @JvmField
    public val BANNER: BlockEntityType = get("banner")
    @JvmField
    public val STRUCTURE_BLOCK: BlockEntityType = get("structure_block")
    @JvmField
    public val END_GATEWAY: BlockEntityType = get("end_gateway")
    @JvmField
    public val COMMAND_BLOCK: BlockEntityType = get("command_block")
    @JvmField
    public val SHULKER_BOX: BlockEntityType = get("shulker_box")
    @JvmField
    public val BED: BlockEntityType = get("bed")
    @JvmField
    public val CONDUIT: BlockEntityType = get("conduit")
    @JvmField
    public val BARREL: BlockEntityType = get("barrel")
    @JvmField
    public val SMOKER: BlockEntityType = get("smoker")
    @JvmField
    public val BLAST_FURNACE: BlockEntityType = get("blast_furnace")
    @JvmField
    public val LECTERN: BlockEntityType = get("lectern")
    @JvmField
    public val BELL: BlockEntityType = get("bell")
    @JvmField
    public val JIGSAW: BlockEntityType = get("jigsaw")
    @JvmField
    public val CAMPFIRE: BlockEntityType = get("campfire")
    @JvmField
    public val BEEHIVE: BlockEntityType = get("beehive")
    @JvmField
    public val SCULK_SENSOR: BlockEntityType = get("sculk_sensor")
    @JvmField
    public val SCULK_CATALYST: BlockEntityType = get("sculk_catalyst")
    @JvmField
    public val SCULK_SHRIEKER: BlockEntityType = get("sculk_shrieker")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): BlockEntityType = Registries.BLOCK_ENTITY_TYPE[Key.key(key)]!!
}

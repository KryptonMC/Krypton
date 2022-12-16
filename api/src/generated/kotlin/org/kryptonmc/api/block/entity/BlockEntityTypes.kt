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
import org.kryptonmc.api.block.entity.container.Barrel
import org.kryptonmc.api.block.entity.container.BlastFurnace
import org.kryptonmc.api.block.entity.container.BrewingStand
import org.kryptonmc.api.block.entity.container.Chest
import org.kryptonmc.api.block.entity.container.Dispenser
import org.kryptonmc.api.block.entity.container.Dropper
import org.kryptonmc.api.block.entity.container.Furnace
import org.kryptonmc.api.block.entity.container.Hopper
import org.kryptonmc.api.block.entity.container.ShulkerBox
import org.kryptonmc.api.block.entity.container.Smoker
import org.kryptonmc.api.block.entity.container.TrappedChest
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.internal.annotations.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(BlockEntityType::class)
public object BlockEntityTypes {

    // @formatter:off
    @JvmField
    public val FURNACE: BlockEntityType<Furnace> = get("furnace")
    @JvmField
    public val CHEST: BlockEntityType<Chest> = get("chest")
    @JvmField
    public val TRAPPED_CHEST: BlockEntityType<TrappedChest> = get("trapped_chest")
    @JvmField
    public val ENDER_CHEST: BlockEntityType<EnderChest> = get("ender_chest")
    @JvmField
    public val JUKEBOX: BlockEntityType<Jukebox> = get("jukebox")
    @JvmField
    public val DISPENSER: BlockEntityType<Dispenser> = get("dispenser")
    @JvmField
    public val DROPPER: BlockEntityType<Dropper> = get("dropper")
    @JvmField
    public val SIGN: BlockEntityType<Sign> = get("sign")
    @JvmField
    public val MOB_SPAWNER: BlockEntityType<MobSpawner> = get("mob_spawner")
    @JvmField
    public val PISTON: BlockEntityType<MovingPiston> = get("piston")
    @JvmField
    public val BREWING_STAND: BlockEntityType<BrewingStand> = get("brewing_stand")
    @JvmField
    public val ENCHANTING_TABLE: BlockEntityType<EnchantmentTable> = get("enchanting_table")
    @JvmField
    public val END_PORTAL: BlockEntityType<EndPortal> = get("end_portal")
    @JvmField
    public val BEACON: BlockEntityType<Beacon> = get("beacon")
    @JvmField
    public val SKULL: BlockEntityType<Skull> = get("skull")
    @JvmField
    public val DAYLIGHT_DETECTOR: BlockEntityType<DaylightDetector> = get("daylight_detector")
    @JvmField
    public val HOPPER: BlockEntityType<Hopper> = get("hopper")
    @JvmField
    public val COMPARATOR: BlockEntityType<Comparator> = get("comparator")
    @JvmField
    public val BANNER: BlockEntityType<Banner> = get("banner")
    @JvmField
    public val STRUCTURE_BLOCK: BlockEntityType<StructureBlock> = get("structure_block")
    @JvmField
    public val END_GATEWAY: BlockEntityType<EndGateway> = get("end_gateway")
    @JvmField
    public val COMMAND_BLOCK: BlockEntityType<CommandBlock> = get("command_block")
    @JvmField
    public val SHULKER_BOX: BlockEntityType<ShulkerBox> = get("shulker_box")
    @JvmField
    public val BED: BlockEntityType<Bed> = get("bed")
    @JvmField
    public val CONDUIT: BlockEntityType<Conduit> = get("conduit")
    @JvmField
    public val BARREL: BlockEntityType<Barrel> = get("barrel")
    @JvmField
    public val SMOKER: BlockEntityType<Smoker> = get("smoker")
    @JvmField
    public val BLAST_FURNACE: BlockEntityType<BlastFurnace> = get("blast_furnace")
    @JvmField
    public val LECTERN: BlockEntityType<Lectern> = get("lectern")
    @JvmField
    public val BELL: BlockEntityType<Bell> = get("bell")
    @JvmField
    public val JIGSAW: BlockEntityType<Jigsaw> = get("jigsaw")
    @JvmField
    public val CAMPFIRE: BlockEntityType<Campfire> = get("campfire")
    @JvmField
    public val BEEHIVE: BlockEntityType<Beehive> = get("beehive")
    @JvmField
    public val SCULK_SENSOR: BlockEntityType<SculkSensor> = get("sculk_sensor")
    @JvmField
    public val SCULK_CATALYST: BlockEntityType<SculkCatalyst> = get("sculk_catalyst")
    @JvmField
    public val SCULK_SHRIEKER: BlockEntityType<SculkShrieker> = get("sculk_shrieker")

    // @formatter:on
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <T : BlockEntity> get(key: String): BlockEntityType<T> = Registries.BLOCK_ENTITY_TYPE.get(Key.key(key))!! as BlockEntityType<T>
}

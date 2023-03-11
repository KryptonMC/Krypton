/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All the built-in vanilla block entity types.
 */
@Catalogue(BlockEntityType::class)
public object BlockEntityTypes {

    // @formatter:off
    @JvmField
    public val FURNACE: RegistryReference<BlockEntityType<Furnace>> = of("furnace")
    @JvmField
    public val CHEST: RegistryReference<BlockEntityType<Chest>> = of("chest")
    @JvmField
    public val TRAPPED_CHEST: RegistryReference<BlockEntityType<TrappedChest>> = of("trapped_chest")
    @JvmField
    public val ENDER_CHEST: RegistryReference<BlockEntityType<EnderChest>> = of("ender_chest")
    @JvmField
    public val JUKEBOX: RegistryReference<BlockEntityType<Jukebox>> = of("jukebox")
    @JvmField
    public val DISPENSER: RegistryReference<BlockEntityType<Dispenser>> = of("dispenser")
    @JvmField
    public val DROPPER: RegistryReference<BlockEntityType<Dropper>> = of("dropper")
    @JvmField
    public val SIGN: RegistryReference<BlockEntityType<Sign>> = of("sign")
    @JvmField
    public val MOB_SPAWNER: RegistryReference<BlockEntityType<MobSpawner>> = of("mob_spawner")
    @JvmField
    public val PISTON: RegistryReference<BlockEntityType<MovingPiston>> = of("piston")
    @JvmField
    public val BREWING_STAND: RegistryReference<BlockEntityType<BrewingStand>> = of("brewing_stand")
    @JvmField
    public val ENCHANTING_TABLE: RegistryReference<BlockEntityType<EnchantmentTable>> = of("enchanting_table")
    @JvmField
    public val END_PORTAL: RegistryReference<BlockEntityType<EndPortal>> = of("end_portal")
    @JvmField
    public val BEACON: RegistryReference<BlockEntityType<Beacon>> = of("beacon")
    @JvmField
    public val SKULL: RegistryReference<BlockEntityType<Skull>> = of("skull")
    @JvmField
    public val DAYLIGHT_DETECTOR: RegistryReference<BlockEntityType<DaylightDetector>> = of("daylight_detector")
    @JvmField
    public val HOPPER: RegistryReference<BlockEntityType<Hopper>> = of("hopper")
    @JvmField
    public val COMPARATOR: RegistryReference<BlockEntityType<Comparator>> = of("comparator")
    @JvmField
    public val BANNER: RegistryReference<BlockEntityType<Banner>> = of("banner")
    @JvmField
    public val STRUCTURE_BLOCK: RegistryReference<BlockEntityType<StructureBlock>> = of("structure_block")
    @JvmField
    public val END_GATEWAY: RegistryReference<BlockEntityType<EndGateway>> = of("end_gateway")
    @JvmField
    public val COMMAND_BLOCK: RegistryReference<BlockEntityType<CommandBlock>> = of("command_block")
    @JvmField
    public val SHULKER_BOX: RegistryReference<BlockEntityType<ShulkerBox>> = of("shulker_box")
    @JvmField
    public val BED: RegistryReference<BlockEntityType<Bed>> = of("bed")
    @JvmField
    public val CONDUIT: RegistryReference<BlockEntityType<Conduit>> = of("conduit")
    @JvmField
    public val BARREL: RegistryReference<BlockEntityType<Barrel>> = of("barrel")
    @JvmField
    public val SMOKER: RegistryReference<BlockEntityType<Smoker>> = of("smoker")
    @JvmField
    public val BLAST_FURNACE: RegistryReference<BlockEntityType<BlastFurnace>> = of("blast_furnace")
    @JvmField
    public val LECTERN: RegistryReference<BlockEntityType<Lectern>> = of("lectern")
    @JvmField
    public val BELL: RegistryReference<BlockEntityType<Bell>> = of("bell")
    @JvmField
    public val JIGSAW: RegistryReference<BlockEntityType<Jigsaw>> = of("jigsaw")
    @JvmField
    public val CAMPFIRE: RegistryReference<BlockEntityType<Campfire>> = of("campfire")
    @JvmField
    public val BEEHIVE: RegistryReference<BlockEntityType<Beehive>> = of("beehive")
    @JvmField
    public val SCULK_SENSOR: RegistryReference<BlockEntityType<SculkSensor>> = of("sculk_sensor")
    @JvmField
    public val SCULK_CATALYST: RegistryReference<BlockEntityType<SculkCatalyst>> = of("sculk_catalyst")
    @JvmField
    public val SCULK_SHRIEKER: RegistryReference<BlockEntityType<SculkShrieker>> = of("sculk_shrieker")

    // @formatter:on
    @JvmStatic
    private fun <T : BlockEntity> of(name: String): RegistryReference<BlockEntityType<T>> =
        RegistryReference.of(Registries.BLOCK_ENTITY_TYPE, Key.key(name))
}

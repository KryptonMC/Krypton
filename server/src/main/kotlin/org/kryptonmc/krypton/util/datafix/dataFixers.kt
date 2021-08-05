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
package org.kryptonmc.krypton.util.datafix

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFixer
import com.mojang.datafixers.DataFixerBuilder
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.util.BOOTSTRAP_EXECUTOR
import org.kryptonmc.krypton.util.datafix.fixes.AddChoices
import org.kryptonmc.krypton.util.datafix.fixes.AdvancementsFix
import org.kryptonmc.krypton.util.datafix.fixes.AlignBitStorageFix
import org.kryptonmc.krypton.util.datafix.fixes.BedInjectorFix
import org.kryptonmc.krypton.util.datafix.fixes.BiomeFix
import org.kryptonmc.krypton.util.datafix.fixes.BlockEntityCustomNameFix
import org.kryptonmc.krypton.util.datafix.fixes.BlockEntityIdFix
import org.kryptonmc.krypton.util.datafix.fixes.BlockFlatteningFix
import org.kryptonmc.krypton.util.datafix.fixes.ChunkBiomeFix
import org.kryptonmc.krypton.util.datafix.fixes.ChunkPalettedStorageFix
import org.kryptonmc.krypton.util.datafix.fixes.ChunkRemoveLightFix
import org.kryptonmc.krypton.util.datafix.fixes.ChunkStatusFix
import org.kryptonmc.krypton.util.datafix.fixes.ChunkStatusFix2
import org.kryptonmc.krypton.util.datafix.fixes.ChunkToProtoChunkFix
import org.kryptonmc.krypton.util.datafix.fixes.FlatGeneratorInfoFix
import org.kryptonmc.krypton.util.datafix.fixes.ForcePOIRebuildFix
import org.kryptonmc.krypton.util.datafix.fixes.FurnaceRecipesFix
import org.kryptonmc.krypton.util.datafix.fixes.GeneratorOptionsFix
import org.kryptonmc.krypton.util.datafix.fixes.LeavesFix
import org.kryptonmc.krypton.util.datafix.fixes.MapIdFix
import org.kryptonmc.krypton.util.datafix.fixes.MissingDimensionFix
import org.kryptonmc.krypton.util.datafix.fixes.NewVillageFix
import org.kryptonmc.krypton.util.datafix.fixes.ObjectiveDisplayNameFix
import org.kryptonmc.krypton.util.datafix.fixes.ObjectiveRenderTypeFix
import org.kryptonmc.krypton.util.datafix.fixes.PickupArrowFix
import org.kryptonmc.krypton.util.datafix.fixes.RecipesFix
import org.kryptonmc.krypton.util.datafix.fixes.RedstoneWireConnectionsFix
import org.kryptonmc.krypton.util.datafix.fixes.RemoveIglooMetadataFix
import org.kryptonmc.krypton.util.datafix.fixes.ReorganizePOIFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameBlockFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameBlockWithJigsawFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameCauldronFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameItemFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameStatsFix
import org.kryptonmc.krypton.util.datafix.fixes.SavedDataPoolElementFeatureFix
import org.kryptonmc.krypton.util.datafix.fixes.StatsCounterFix
import org.kryptonmc.krypton.util.datafix.fixes.StructureReferenceCountFix
import org.kryptonmc.krypton.util.datafix.fixes.StructureTemplateBlockStateFix
import org.kryptonmc.krypton.util.datafix.fixes.TeamDisplayNameFix
import org.kryptonmc.krypton.util.datafix.fixes.TrappedChestBlockEntityFix
import org.kryptonmc.krypton.util.datafix.fixes.VillageCropFix
import org.kryptonmc.krypton.util.datafix.fixes.WallPropertyFix
import org.kryptonmc.krypton.util.datafix.fixes.WorldGenSettingsFix
import org.kryptonmc.krypton.util.datafix.fixes.WriteAndReadFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.BannerBlockEntityColorFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.BlockEntityBlockStateFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.CatTypeFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.ColorlessShulkerEntityFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.EntityBlockStateFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.EntityCustomNameFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.EntityIdFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.ExpiringMemoryDataFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.GossipUUIDFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.HealthFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.HorseSaddleFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.ItemFrameDirectionFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.ItemFramePaintingDirectionFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.JigsawPropertiesFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.JigsawRotationFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.JukeboxFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.KeepPackedFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.MinecartIdentifiersFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.MobSpawnerIdentifiersFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.NamedEntityFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.PaintingMotiveFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.ProjectileOwnerFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.RebuildVillagerLevelAndXpFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.RebuildZombieVillagerXpFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.RedundantChanceTagsFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.RemoveGolemGossipFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.RenameOminousBannerBlockEntityFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.RidingToPassengersFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.ShulkerBoxBlockEntityColorFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.ShulkerColorFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.ShulkerRotationFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.SilentArmorStandEntityFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.SplitCatFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.SplitElderGuardianFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.SplitEquipmentFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.SplitHorseFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.SplitSkeletonFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.SplitZombieFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.StrictJsonSignTextFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.StriderGravityFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.VillagerDataFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.VillagerFollowRangeFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.VillagerTradeFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.WolfColorFix
import org.kryptonmc.krypton.util.datafix.fixes.entity.ZombieVillagerTypeFix
import org.kryptonmc.krypton.util.datafix.fixes.fixCustomNameTag
import org.kryptonmc.krypton.util.datafix.fixes.item.BannerColorFix
import org.kryptonmc.krypton.util.datafix.fixes.item.BedColorFix
import org.kryptonmc.krypton.util.datafix.fixes.item.EnchantmentNamesFix
import org.kryptonmc.krypton.util.datafix.fixes.item.ItemCustomNameFix
import org.kryptonmc.krypton.util.datafix.fixes.item.ItemFlatteningFix
import org.kryptonmc.krypton.util.datafix.fixes.item.ItemIdFix
import org.kryptonmc.krypton.util.datafix.fixes.item.ItemLoreFix
import org.kryptonmc.krypton.util.datafix.fixes.item.MapItemIdFix
import org.kryptonmc.krypton.util.datafix.fixes.item.PotionFix
import org.kryptonmc.krypton.util.datafix.fixes.item.ShulkerBoxItemColorFix
import org.kryptonmc.krypton.util.datafix.fixes.item.SpawnEggItemFix
import org.kryptonmc.krypton.util.datafix.fixes.item.SpawnEggItemStackFix
import org.kryptonmc.krypton.util.datafix.fixes.item.StrictJsonWrittenBookPagesFix
import org.kryptonmc.krypton.util.datafix.fixes.item.WaterPotionFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RENAMED_DYE_ITEMS
import org.kryptonmc.krypton.util.datafix.fixes.name.RENAMED_CORALS
import org.kryptonmc.krypton.util.datafix.fixes.name.RENAMED_CORAL_FANS
import org.kryptonmc.krypton.util.datafix.fixes.name.RecipeFlatteningFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameAdvancementsFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameAttributesFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameBeehivePOIFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameBiomesFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameChunkStructuresTemplateFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameCodSalmonFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameEntitiesFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameHeightmapsFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameOminousBannerFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenamePufferfishFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameRavagerFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameRecipesFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameTippedArrowFix
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameZombiePigmanFix
import org.kryptonmc.krypton.util.datafix.fixes.uuid.BlockEntityUUIDFix
import org.kryptonmc.krypton.util.datafix.fixes.uuid.EntityUUIDFix
import org.kryptonmc.krypton.util.datafix.fixes.uuid.ItemStackUUIDFix
import org.kryptonmc.krypton.util.datafix.fixes.uuid.PlayerUUIDFix
import org.kryptonmc.krypton.util.datafix.fixes.uuid.SavedDataUUIDFix
import org.kryptonmc.krypton.util.datafix.fixes.uuid.StringUUIDFix
import org.kryptonmc.krypton.util.datafix.fixes.uuid.WorldUUIDFix
import org.kryptonmc.krypton.util.datafix.schema.InitialSchema
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema
import org.kryptonmc.krypton.util.datafix.schema.V100
import org.kryptonmc.krypton.util.datafix.schema.V102
import org.kryptonmc.krypton.util.datafix.schema.V1022
import org.kryptonmc.krypton.util.datafix.schema.V106
import org.kryptonmc.krypton.util.datafix.schema.V107
import org.kryptonmc.krypton.util.datafix.schema.V1125
import org.kryptonmc.krypton.util.datafix.schema.V135
import org.kryptonmc.krypton.util.datafix.schema.V143
import org.kryptonmc.krypton.util.datafix.schema.V1451
import org.kryptonmc.krypton.util.datafix.schema.V1451S1
import org.kryptonmc.krypton.util.datafix.schema.V1451S2
import org.kryptonmc.krypton.util.datafix.schema.V1451S3
import org.kryptonmc.krypton.util.datafix.schema.V1451S4
import org.kryptonmc.krypton.util.datafix.schema.V1451S5
import org.kryptonmc.krypton.util.datafix.schema.V1451S6
import org.kryptonmc.krypton.util.datafix.schema.V1451S7
import org.kryptonmc.krypton.util.datafix.schema.V1460
import org.kryptonmc.krypton.util.datafix.schema.V1466
import org.kryptonmc.krypton.util.datafix.schema.V1470
import org.kryptonmc.krypton.util.datafix.schema.V1481
import org.kryptonmc.krypton.util.datafix.schema.V1483
import org.kryptonmc.krypton.util.datafix.schema.V1486
import org.kryptonmc.krypton.util.datafix.schema.V1510
import org.kryptonmc.krypton.util.datafix.schema.V1800
import org.kryptonmc.krypton.util.datafix.schema.V1801
import org.kryptonmc.krypton.util.datafix.schema.V1904
import org.kryptonmc.krypton.util.datafix.schema.V1906
import org.kryptonmc.krypton.util.datafix.schema.V1909
import org.kryptonmc.krypton.util.datafix.schema.V1920
import org.kryptonmc.krypton.util.datafix.schema.V1928
import org.kryptonmc.krypton.util.datafix.schema.V1929
import org.kryptonmc.krypton.util.datafix.schema.V1931
import org.kryptonmc.krypton.util.datafix.schema.V2100
import org.kryptonmc.krypton.util.datafix.schema.V2501
import org.kryptonmc.krypton.util.datafix.schema.V2502
import org.kryptonmc.krypton.util.datafix.schema.V2505
import org.kryptonmc.krypton.util.datafix.schema.V2509
import org.kryptonmc.krypton.util.datafix.schema.V2519
import org.kryptonmc.krypton.util.datafix.schema.V2522
import org.kryptonmc.krypton.util.datafix.schema.V2551
import org.kryptonmc.krypton.util.datafix.schema.V2568
import org.kryptonmc.krypton.util.datafix.schema.V2571
import org.kryptonmc.krypton.util.datafix.schema.V2684
import org.kryptonmc.krypton.util.datafix.schema.V2686
import org.kryptonmc.krypton.util.datafix.schema.V2688
import org.kryptonmc.krypton.util.datafix.schema.V2704
import org.kryptonmc.krypton.util.datafix.schema.V2707
import org.kryptonmc.krypton.util.datafix.schema.V501
import org.kryptonmc.krypton.util.datafix.schema.V700
import org.kryptonmc.krypton.util.datafix.schema.V701
import org.kryptonmc.krypton.util.datafix.schema.V702
import org.kryptonmc.krypton.util.datafix.schema.V703
import org.kryptonmc.krypton.util.datafix.schema.V704
import org.kryptonmc.krypton.util.datafix.schema.V705
import org.kryptonmc.krypton.util.datafix.schema.V808
import org.kryptonmc.krypton.util.datafix.schema.ensureNamespaced

private val SAME = ::Schema
private val SAME_NAMESPACED: (Int, Schema) -> Schema = ::NamespacedSchema

val DATA_FIXER: DataFixer = DataFixerBuilder(KryptonPlatform.worldVersion).apply {
    addSchema(99, ::InitialSchema)
    addSchema(100, ::V100).apply {
        addFixer(SplitEquipmentFix(this, true))
    }
    addSchema(101, SAME).apply {
        addFixer(StrictJsonSignTextFix(this, false))
    }
    addSchema(102, ::V102).apply {
        addFixer(ItemIdFix(this, true))
        addFixer(PotionFix(this, false))
    }
    addSchema(105, SAME).apply {
        addFixer(SpawnEggItemFix(this, true))
    }
    addSchema(106, ::V106).apply {
        addFixer(MobSpawnerIdentifiersFix(this, true))
    }
    addSchema(107, ::V107).apply {
        addFixer(MinecartIdentifiersFix(this, true))
    }
    addSchema(108, SAME).apply {
        addFixer(StringUUIDFix(this, true))
    }
    addSchema(109, SAME).apply {
        addFixer(HealthFix(this, true))
    }
    addSchema(110, SAME).apply {
        addFixer(HorseSaddleFix(this, true))
    }
    addSchema(111, SAME).apply {
        addFixer(ItemFramePaintingDirectionFix(this, true))
    }
    addSchema(113, SAME).apply {
        addFixer(RedundantChanceTagsFix(this, true))
    }
    addSchema(135, ::V135).apply {
        addFixer(RidingToPassengersFix(this, true))
    }
    addSchema(143, ::V143).apply {
        addFixer(RenameTippedArrowFix(this, true))
    }
    addSchema(147, SAME).apply {
        addFixer(SilentArmorStandEntityFix(this, true))
    }
    addSchema(165, SAME).apply {
        addFixer(StrictJsonWrittenBookPagesFix(this, true))
    }
    addSchema(501, ::V501).apply {
        addFixer(AddChoices(this, "Add 1.10 entities fix", References.ENTITY))
    }
    addSchema(502, SAME).apply {
        addFixer(RenameItemFix(this, "cooked_fish item renamer") { if (it.ensureNamespaced() == "minecraft:cooked_fished") "minecraft:cooked_fish" else it })
        addFixer(ZombieVillagerTypeFix(this, false))
    }
    addSchema(700, ::V700).apply {
        addFixer(SplitElderGuardianFix(this, true))
    }
    addSchema(701, ::V701).apply {
        addFixer(SplitSkeletonFix(this, true))
    }
    addSchema(702, ::V702).apply {
        addFixer(SplitZombieFix(this, true))
    }
    addSchema(703, ::V703).apply {
        addFixer(SplitHorseFix(this, true))
    }
    addSchema(704, ::V704).apply {
        addFixer(BlockEntityIdFix(this, true))
    }
    addSchema(705, ::V705).apply {
        addFixer(EntityIdFix(this, true))
    }
    addSchema(804, SAME_NAMESPACED).apply {
        addFixer(BannerColorFix(this, true))
    }
    addSchema(806, SAME_NAMESPACED).apply {
        addFixer(WaterPotionFix(this, false))
    }
    addSchema(808, ::V808).apply {
        addFixer(AddChoices(this, "Added shulker box", References.BLOCK_ENTITY))
    }
    addSchema(808, 1, SAME_NAMESPACED).apply {
        addFixer(ShulkerColorFix(this, false))
    }
    addSchema(813, SAME_NAMESPACED).apply {
        addFixer(ShulkerBoxItemColorFix(this, false))
        addFixer(ShulkerBoxBlockEntityColorFix(this, false))
    }
    addSchema(820, SAME_NAMESPACED).apply {
        addFixer(RenameItemFix(this, "Totem item renamer", rename("minecraft:totem" to "minecraft:totem_of_undying")))
    }
    addSchema(1022, ::V1022).apply {
        addFixer(WriteAndReadFix(this, "Added shoulder entities to players", References.PLAYER))
    }
    addSchema(1125, ::V1125).apply {
        addFixer(BedInjectorFix(this, true))
        addFixer(BedColorFix(this, false))
    }
    addSchema(1450, SAME_NAMESPACED).apply {
        addFixer(StructureTemplateBlockStateFix(this, false))
    }
    addSchema(1451, ::V1451).apply {
        addFixer(AddChoices(this, "Add trapped chest", References.BLOCK_ENTITY))
    }
    addSchema(1451, 1, ::V1451S1).apply {
        addFixer(ChunkPalettedStorageFix(this, true))
    }
    addSchema(1451, 2, ::V1451S2).apply {
        addFixer(BlockEntityBlockStateFix(this, true))
    }
    addSchema(1451, 3, ::V1451S3).apply {
        addFixer(EntityBlockStateFix(this, true))
        addFixer(MapItemIdFix(this, false))
    }
    addSchema(1451, 4, ::V1451S4).apply {
        addFixer(BlockFlatteningFix(this, true))
        addFixer(ItemFlatteningFix(this, false))
    }
    addSchema(1451, 5, ::V1451S5).apply {
        addFixer(AddChoices(this, "Remove note block flower pot fix", References.BLOCK_ENTITY))
        addFixer(SpawnEggItemStackFix(this, false))
        addFixer(WolfColorFix(this, false))
        addFixer(BannerBlockEntityColorFix(this, false))
        addFixer(FlatGeneratorInfoFix(this, false))
    }
    addSchema(1451, 6, ::V1451S6).apply {
        addFixer(StatsCounterFix(this, true))
        addFixer(WriteAndReadFix(this, "Rewrite objectives", References.OBJECTIVE))
        addFixer(JukeboxFix(this, false))
    }
    addSchema(1451, 7, ::V1451S7).apply {
        addFixer(VillageCropFix(this, true))
    }
    addSchema(1451, 7, SAME_NAMESPACED).apply {
        addFixer(VillagerTradeFix(this, false))
    }
    addSchema(1456, SAME_NAMESPACED).apply {
        addFixer(ItemFrameDirectionFix(this, false))
    }
    addSchema(1458, SAME_NAMESPACED).apply {
        addFixer(EntityCustomNameFix(this, false))
        addFixer(ItemCustomNameFix(this, false))
        addFixer(BlockEntityCustomNameFix(this, false))
    }
    addSchema(1460, ::V1460).apply {
        addFixer(PaintingMotiveFix(this, false))
    }
    addSchema(1466, ::V1466).apply {
        addFixer(ChunkToProtoChunkFix(this, true))
    }
    addSchema(1470, ::V1470).apply {
        addFixer(AddChoices(this, "Add 1.13 entities fix", References.ENTITY))
    }
    addSchema(1474, SAME_NAMESPACED).apply {
        addFixer(ColorlessShulkerEntityFix(this, false))
        addFixer(RenameBlockFix(this, "Colourless shulker block fixer") { if (it.ensureNamespaced() == "minecraft:purple_shulker_box") "minecraft:shulker_box" else it })
        addFixer(RenameItemFix(this, "Colourless shulker item fixer") { if (it.ensureNamespaced() == "minecraft:purple_shulker_box") "minecraft:shulker_box" else it })
    }
    addSchema(1475, SAME_NAMESPACED).apply {
        addFixer(RenameBlockFix(this, "Flowing fixer", rename(mapOf(
            "minecraft:flowing_water" to "minecraft:water",
            "minecraft:flowing_lava" to "minecraft:lava"
        ))))
    }
    addSchema(1480, SAME_NAMESPACED).apply {
        addFixer(RenameBlockFix(this, "Rename coral blocks", rename(RENAMED_CORALS)))
        addFixer(RenameItemFix(this, "Rename coral items", rename(RENAMED_CORALS)))
    }
    addSchema(1481, ::V1481).apply {
        addFixer(AddChoices(this, "Add conduit", References.BLOCK_ENTITY))
    }
    addSchema(1483, ::V1483).apply {
        addFixer(RenamePufferfishFix(this, true))
        addFixer(RenameItemFix(this, "Rename pufferfish egg items", rename(RenamePufferfishFix.RENAMED_IDS)))
    }
    addSchema(1484, SAME_NAMESPACED).apply {
        val seagrassRenameMap = mapOf(
            "minecraft:sea_grass" to "minecraft:seagrass",
            "minecraft:tall_sea_grass" to "minecraft:tall_seagrass"
        )
        addFixer(RenameItemFix(this, "Rename seagrass items", rename(seagrassRenameMap)))
        addFixer(RenameBlockFix(this, "Rename seagrass blocks", rename(seagrassRenameMap)))
        addFixer(RenameHeightmapsFix(this, false))
    }
    addSchema(1486, ::V1486).apply {
        addFixer(RenameCodSalmonFix(this, true))
        addFixer(RenameItemFix(this, "Rename cod/salmon egg items", rename(RenameCodSalmonFix.RENAMED_EGGS)))
    }
    addSchema(1487, SAME_NAMESPACED).apply {
        val prismarineRenames = mapOf(
            "minecraft:prismarine_bricks_slab" to "minecraft:prismarine_brick_slab",
            "minecraft:prismarine_bricks_stairs" to "minecraft:prismarine_brick_stairs"
        )
        addFixer(RenameItemFix(this, "Rename prismarine_brick(s)_* items", rename(prismarineRenames)))
        addFixer(RenameBlockFix(this, "Rename prismarine_brick(s)_* blocks", rename(prismarineRenames)))
    }
    addSchema(1488, SAME_NAMESPACED).apply {
        addFixer(RenameBlockFix(this, "Rename kelp/kelp_top", rename(mapOf("minecraft:kelp_top" to "minecraft:kelp", "minecraft:kelp" to "minecraft:kelp_plant"))))
        addFixer(RenameItemFix(this, "Rename kelp_top", rename("minecraft:kelp_top" to "minecraft:kelp")))
        addFixer(object : NamedEntityFix(this, false, "Command block block entity custom name fix", References.BLOCK_ENTITY, "minecraft:command_block") {
            override fun fix(typed: Typed<*>): Typed<*> = typed.update(remainderFinder()) { it.fixCustomNameTag() }
        })
        addFixer(object : NamedEntityFix(this, false, "Command block minecart custom name fix", References.ENTITY, "minecraft:commandblock_minecart") {
            override fun fix(typed: Typed<*>): Typed<*> = typed.update(remainderFinder()) { it.fixCustomNameTag() }
        })
        addFixer(RemoveIglooMetadataFix(this, false))
    }
    addSchema(1490, SAME_NAMESPACED).apply {
        addFixer(RenameBlockFix(this, "Rename melon_block", rename("minecraft:melon_block" to "minecraft:melon")))
        addFixer(RenameItemFix(this, "Rename melon_block/melon/speckled_melon", rename(mapOf(
            "minecraft:melon_block" to "minecraft:melon",
            "minecraft:melon" to "minecraft:melon_slice",
            "minecraft:speckled_melon" to "minecraft:glistering_melon_slice"
        ))))
    }
    addSchema(1492, SAME_NAMESPACED).apply {
        addFixer(RenameChunkStructuresTemplateFix(this, false))
    }
    addSchema(1494, SAME_NAMESPACED).apply {
        addFixer(EnchantmentNamesFix(this, false))
    }
    addSchema(1496, SAME_NAMESPACED).apply {
        addFixer(LeavesFix(this, false))
    }
    addSchema(1500, SAME_NAMESPACED).apply {
        addFixer(KeepPackedFix(this, false))
    }
    addSchema(1501, SAME_NAMESPACED).apply {
        addFixer(AdvancementsFix(this, false))
    }
    addSchema(1502, SAME_NAMESPACED).apply {
        addFixer(RecipesFix(this, false))
    }
    addSchema(1506, SAME_NAMESPACED).apply {
        addFixer(GeneratorOptionsFix(this, false))
    }
    addSchema(1510, ::V1510).apply {
        addFixer(RenameBlockFix(this, "Block renaming fix", rename(RenameEntitiesFix.REMAPPED_BLOCKS)))
        addFixer(RenameItemFix(this, "Item renaming fix", rename(RenameEntitiesFix.REMAPPED_ITEMS)))
        addFixer(RecipeFlatteningFix(this, false))
        addFixer(RenameEntitiesFix(this, true))
        addFixer(RenameStatsFix(this, "Rename swim stats fix", mapOf(
            "minecraft:swim_one_cm" to "minecraft:walk_on_water_one_cm",
            "minecraft:dive_one_cm" to "minecraft:walk_under_water_one_cm"
        )))
    }
    addSchema(1514, SAME_NAMESPACED).apply {
        addFixer(ObjectiveDisplayNameFix(this, false))
        addFixer(TeamDisplayNameFix(this, false))
        addFixer(ObjectiveRenderTypeFix(this, false))
    }
    addSchema(1515, SAME_NAMESPACED).apply {
        addFixer(RenameBlockFix(this, "Rename coral fan blocks", rename(RENAMED_CORAL_FANS)))
    }
    addSchema(1624, SAME_NAMESPACED).apply {
        addFixer(TrappedChestBlockEntityFix(this, false))
    }
    addSchema(1800, ::V1800).apply {
        addFixer(AddChoices(this, "Added 1.14 mobs fix", References.ENTITY))
        addFixer(RenameItemFix(this, "Rename dye items", rename(RENAMED_DYE_ITEMS)))
    }
    addSchema(1801, ::V1801).apply {
        addFixer(AddChoices(this, "Added Illager Beast", References.ENTITY))
    }
    addSchema(1802, SAME_NAMESPACED).apply {
        addFixer(RenameBlockFix(this, "Rename sign blocks & stone slabs", rename(mapOf(
            "minecraft:stone_slab" to "minecraft:smooth_stone_slab",
            "minecraft:sign" to "minecraft:oak_sign",
            "minecraft:wall_sign" to "minecraft:oak_wall_sign"
        ))))
        addFixer(RenameItemFix(this, "Rename sign items & stone slabs", rename(mapOf(
            "minecraft:stone_slab" to "minecraft:smooth_stone_slab",
            "minecraft:sign" to "minecraft:oak_sign"
        ))))
    }
    addSchema(1803, SAME_NAMESPACED).apply {
        addFixer(ItemLoreFix(this, false))
    }
    addSchema(1904, ::V1904).apply {
        addFixer(AddChoices(this, "Added Cats", References.ENTITY))
        addFixer(SplitCatFix(this, false))
    }
    addSchema(1905, SAME_NAMESPACED).apply {
        addFixer(ChunkStatusFix(this, false))
    }
    addSchema(1906, ::V1906).apply {
        addFixer(AddChoices(this, "Add POI blocks", References.BLOCK_ENTITY))
    }
    addSchema(1909, ::V1909).apply {
        addFixer(AddChoices(this, "Add jigsaw", References.BLOCK_ENTITY))
    }
    addSchema(1911, SAME_NAMESPACED).apply {
        addFixer(ChunkStatusFix2(this, false))
    }
    addSchema(1917, SAME_NAMESPACED).apply {
        addFixer(CatTypeFix(this, false))
    }
    addSchema(1918, SAME_NAMESPACED).apply {
        addFixer(VillagerDataFix(this, "minecraft:villager"))
        addFixer(VillagerDataFix(this, "minecraft:zombie_villager"))
    }
    addSchema(1920, ::V1920).apply {
        addFixer(NewVillageFix(this, false))
        addFixer(AddChoices(this, "Add campfire", References.BLOCK_ENTITY))
    }
    addSchema(1925, SAME_NAMESPACED).apply {
        addFixer(MapIdFix(this, false))
    }
    addSchema(1928, ::V1928).apply {
        addFixer(RenameRavagerFix(this, true))
        addFixer(RenameItemFix(this, "Rename ravager egg item", rename(RenameRavagerFix.REMAPPED_IDS)))
    }
    addSchema(1929, ::V1929).apply {
        addFixer(AddChoices(this, "Add Wandering Trader and Trader Llama", References.ENTITY))
    }
    addSchema(1931, ::V1931).apply {
        addFixer(AddChoices(this, "Added Fox", References.ENTITY))
    }
    addSchema(1946, SAME_NAMESPACED).apply {
        addFixer(ReorganizePOIFix(this, false))
    }
    addSchema(1948, SAME_NAMESPACED).apply {
        addFixer(RenameOminousBannerFix(this, false))
    }
    addSchema(1953, SAME_NAMESPACED).apply {
        addFixer(RenameOminousBannerBlockEntityFix(this, false))
    }
    addSchema(1955, SAME_NAMESPACED).apply {
        addFixer(RebuildVillagerLevelAndXpFix(this, false))
        addFixer(RebuildZombieVillagerXpFix(this, false))
    }
    addSchema(1961, SAME_NAMESPACED).apply {
        addFixer(ChunkRemoveLightFix(this, false))
    }
    addSchema(1963, SAME_NAMESPACED).apply {
        addFixer(RemoveGolemGossipFix(this, false))
    }
    addSchema(2100, ::V2100).apply {
        addFixer(AddChoices(this, "Added Bee and Bee Stinger", References.ENTITY))
        addFixer(AddChoices(this, "Add beehive", References.BLOCK_ENTITY))
        addFixer(RenameRecipesFix(this, false, "Rename sugar recipe", rename("minecraft:sugar" to "sugar_from_sugar_cane")))
        addFixer(RenameAdvancementsFix(this, false, "Rename sugar recipe advancement", rename("minecraft:recipes/misc/sugar" to "minecraft:recipes/misc/sugar_from_sugar_cane")))
    }
    addSchema(2202, SAME_NAMESPACED).apply {
        addFixer(ChunkBiomeFix(this, false))
    }
    addSchema(2209, SAME_NAMESPACED).apply {
        addFixer(RenameItemFix(this, "Rename bee_hive item to beehive", rename("minecraft:bee_hive" to "minecraft:beehive")))
        addFixer(RenameBeehivePOIFix(this))
        addFixer(RenameBlockFix(this, "Rename bee_hive block to beehive", rename("minecraft:bee_hive" to "minecraft:beehive")))
    }
    addSchema(2211, SAME_NAMESPACED).apply {
        addFixer(StructureReferenceCountFix(this, false))
    }
    addSchema(2218, SAME_NAMESPACED).apply {
        addFixer(ForcePOIRebuildFix(this, false))
    }
    addSchema(2501, ::V2501).apply {
        addFixer(FurnaceRecipesFix(this, true))
    }
    addSchema(2502, ::V2502).apply {
        addFixer(AddChoices(this, "Added Hoglin", References.ENTITY))
    }
    addSchema(2503, SAME_NAMESPACED).apply {
        addFixer(WallPropertyFix(this, false))
        addFixer(RenameAdvancementsFix(this, false, "Composter category change", rename("minecraft:recipes/misc/composter" to "minecraft:recipes/decorations/composter")))
    }
    addSchema(2505, ::V2505).apply {
        addFixer(AddChoices(this, "Added Piglin", References.ENTITY))
        addFixer(ExpiringMemoryDataFix(this, "minecraft:villager"))
    }
    addSchema(2508, SAME_NAMESPACED).apply {
        val fungiRenames = mapOf(
            "minecraft:warped_fungi" to "minecraft:warped_fungus",
            "minecraft:crimson_fungi" to "minecraft:crimson_fungus"
        )
        addFixer(RenameItemFix(this, "Renamed fungi items to fungus", rename(fungiRenames)))
        addFixer(RenameBlockFix(this, "Renamed fungi blocks to fungus", rename(fungiRenames)))
    }
    addSchema(2509, ::V2509).apply {
        addFixer(RenameZombiePigmanFix(this))
        addFixer(RenameItemFix(this, "Rename zombie pigman egg item", rename(RenameZombiePigmanFix.REMAPPED_IDS)))
    }
    addSchema(2511, SAME_NAMESPACED).apply {
        addFixer(ProjectileOwnerFix(this))
    }
    addSchema(2514, SAME_NAMESPACED).apply {
        addFixer(EntityUUIDFix(this))
        addFixer(BlockEntityUUIDFix(this))
        addFixer(PlayerUUIDFix(this))
        addFixer(WorldUUIDFix(this))
        addFixer(SavedDataUUIDFix(this))
        addFixer(ItemStackUUIDFix(this))
    }
    addSchema(2516, SAME_NAMESPACED).apply {
        addFixer(GossipUUIDFix(this, "minecraft:villager"))
        addFixer(GossipUUIDFix(this, "minecraft:zombie_villager"))
    }
    addSchema(2518, SAME_NAMESPACED).apply {
        addFixer(JigsawPropertiesFix(this, false))
        addFixer(JigsawRotationFix(this, false))
    }
    addSchema(2519, ::V2519).apply {
        addFixer(AddChoices(this, "Added Strider", References.ENTITY))
    }
    addSchema(2522, ::V2522).apply {
        addFixer(AddChoices(this, "Added Zoglin", References.ENTITY))
    }
    addSchema(2523, SAME_NAMESPACED).apply {
        addFixer(RenameAttributesFix(this))
    }
    addSchema(2527, SAME_NAMESPACED).apply {
        addFixer(AlignBitStorageFix(this))
    }
    addSchema(2528, SAME_NAMESPACED).apply {
        addFixer(RenameItemFix(this, "Rename soul fire torch and soul fire lantern", rename(mapOf(
            "minecraft:soul_fire_torch" to "minecraft:soul_torch",
            "minecraft:soul_fire_lantern" to "minecraft:soul_lantern"
        ))))
        addFixer(RenameBlockFix(this, "Rename soul fire torch and soul fire lantern", rename(mapOf(
            "minecraft:soul_fire_torch" to "minecraft:soul_torch",
            "minecraft:soul_fire_wall_torch" to "minecraft:soul_wall_torch",
            "minecraft:soul_fire_lantern" to "minecraft:soul_lantern"
        ))))
    }
    addSchema(2529, SAME_NAMESPACED).apply {
        addFixer(StriderGravityFix(this, false))
    }
    addSchema(2531, SAME_NAMESPACED).apply {
        addFixer(RedstoneWireConnectionsFix(this))
    }
    addSchema(2533, SAME_NAMESPACED).apply {
        addFixer(VillagerFollowRangeFix(this))
    }
    addSchema(2535, SAME_NAMESPACED).apply {
        addFixer(ShulkerRotationFix(this))
    }
    addSchema(2550, SAME_NAMESPACED).apply {
        addFixer(WorldGenSettingsFix(this))
    }
    addSchema(2551, ::V2551).apply {
        addFixer(WriteAndReadFix(this, "Add types to world generation data", References.WORLD_GEN_SETTINGS))
    }
    addSchema(2552, SAME_NAMESPACED).apply {
        addFixer(RenameBiomesFix(this, false, "Nether biome rename", mapOf("minecraft:nether" to "minecraft:nether_wastes")))
    }
    addSchema(2553, SAME_NAMESPACED).apply {
        addFixer(BiomeFix(this, false))
    }
    addSchema(2558, SAME_NAMESPACED).apply {
        addFixer(MissingDimensionFix(this, false))
    }
    addSchema(2568, ::V2568).apply {
        addFixer(AddChoices(this, "Added Piglin Brute", References.ENTITY))
    }
    addSchema(2571, ::V2571).apply {
        addFixer(AddChoices(this, "Added Goat", References.ENTITY))
    }
    addSchema(2679, SAME_NAMESPACED).apply {
        addFixer(RenameCauldronFix(this, false))
    }
    addSchema(2680, SAME_NAMESPACED).apply {
        addFixer(RenameItemFix(this, "Renamed grass path item to dirt path", rename("minecraft:grass_path" to "minecraft:dirt_path")))
        addFixer(RenameBlockWithJigsawFix(this, "Renamed grass path block to dirt path", rename("minecraft:grass_path" to "minecraft:dirt_path")))
    }
    addSchema(2684, ::V2684).apply {
        addFixer(AddChoices(this, "Added Sculk Sensor", References.BLOCK_ENTITY))
    }
    addSchema(2686, ::V2686).apply {
        addFixer(AddChoices(this, "Added Axolotl", References.ENTITY))
    }
    addSchema(2688, ::V2688).apply {
        addFixer(AddChoices(this, "Added Glow Squid", References.ENTITY))
        addFixer(AddChoices(this, "Added Glow Item Frame", References.ENTITY))
    }
    addSchema(2690, SAME_NAMESPACED).apply {
        val oxidizedRenames = mapOf(
            "minecraft:weathered_copper_block" to "minecraft:oxidized_copper_block",
            "minecraft:semi_weathered_copper_block" to "minecraft:weathered_copper_block",
            "minecraft:lightly_weathered_copper_block" to "minecraft:exposed_copper_block",
            "minecraft:weathered_cut_copper" to "minecraft:oxidized_cut_copper",
            "minecraft:semi_weathered_cut_copper" to "minecraft:weathered_cut_copper",
            "minecraft:lightly_weathered_cut_copper" to "minecraft:exposed_cut_copper",
            "minecraft:weathered_cut_copper_stairs" to "minecraft:oxidized_cut_copper_stairs",
            "minecraft:semi_weathered_cut_copper_stairs" to "minecraft:weathered_cut_copper_stairs",
            "minecraft:lightly_weathered_cut_copper_stairs" to "minecraft:exposed_cut_copper_stairs",
            "minecraft:weathered_cut_copper_slab" to "minecraft:oxidized_cut_copper_slab",
            "minecraft:semi_weathered_cut_copper_slab" to "minecraft:weathered_cut_copper_slab",
            "minecraft:lightly_weathered_cut_copper_slab" to "minecraft:exposed_cut_copper_slab",
            "minecraft:waxed_semi_weathered_copper" to "minecraft:waxed_weathered_copper",
            "minecraft:waxed_lightly_weathered_copper" to "minecraft:waxed_exposed_copper",
            "minecraft:waxed_semi_weathered_cut_copper" to "minecraft:waxed_weathered_cut_copper",
            "minecraft:waxed_lightly_weathered_cut_copper" to "minecraft:waxed_exposed_cut_copper",
            "minecraft:waxed_semi_weathered_cut_copper_stairs" to "minecraft:waxed_weathered_cut_copper_stairs",
            "minecraft:waxed_lightly_weathered_cut_copper_stairs" to "minecraft:waxed_exposed_cut_copper_stairs",
            "minecraft:waxed_semi_weathered_cut_copper_slab" to "minecraft:waxed_weathered_cut_copper_slab",
            "minecraft:waxed_lightly_weathered_cut_copper_slab" to "minecraft:waxed_exposed_cut_copper_slab"
        )
        addFixer(RenameItemFix(this, "Renamed copper block items to new oxidized terms", rename(oxidizedRenames)))
        addFixer(RenameBlockWithJigsawFix(this, "Renamed copper blocks to new oxidized terms", rename(oxidizedRenames)))
    }
    addSchema(2691, SAME_NAMESPACED).apply {
        val copperRenames = mapOf(
            "minecraft:waxed_copper" to "minecraft:waxed_copper_block",
            "minecraft:oxidized_copper_block" to "minecraft:oxidized_copper",
            "minecraft:weathered_copper_block" to "minecraft:weathered_copper",
            "minecraft:exposed_copper_block" to "minecraft:exposed_copper"
        )
        addFixer(RenameItemFix(this, "Rename copper item suffixes", rename(copperRenames)))
        addFixer(RenameBlockWithJigsawFix(this, "Rename copper blocks suffixes", rename(copperRenames)))
    }
    addSchema(2696, SAME_NAMESPACED).apply {
        val grimstoneRenames = mapOf(
            "minecraft:grimstone" to "minecraft:deepslate",
            "minecraft:grimstone_slab" to "minecraft:cobbled_deepslate_slab",
            "minecraft:grimstone_stairs" to "minecraft:cobbled_deepslate_stairs",
            "minecraft:grimstone_wall" to "minecraft:cobbled_deepslate_wall",
            "minecraft:polished_grimstone" to "minecraft:polished_deepslate",
            "minecraft:polished_grimstone_slab" to "minecraft:polished_deepslate_slab",
            "minecraft:polished_grimstone_stairs" to "minecraft:polished_deepslate_stairs",
            "minecraft:polished_grimstone_wall" to "minecraft:polished_deepslate_wall",
            "minecraft:grimstone_tiles" to "minecraft:deepslate_tiles",
            "minecraft:grimstone_tile_slab" to "minecraft:deepslate_tile_slab",
            "minecraft:grimstone_tile_stairs" to "minecraft:deepslate_tile_stairs",
            "minecraft:grimstone_tile_wall" to "minecraft:deepslate_tile_wall",
            "minecraft:grimstone_bricks" to "minecraft:deepslate_bricks",
            "minecraft:grimstone_brick_slab" to "minecraft:deepslate_brick_slab",
            "minecraft:grimstone_brick_stairs" to "minecraft:deepslate_brick_stairs",
            "minecraft:grimstone_brick_wall" to "minecraft:deepslate_brick_wall",
            "minecraft:chiseled_grimstone" to "minecraft:chiseled_deepslate"
        )
        addFixer(RenameItemFix(this, "Renamed grimstone block items to deepslate", rename(grimstoneRenames)))
        addFixer(RenameBlockWithJigsawFix(this, "Renamed grimstone blocks to deepslate", rename(grimstoneRenames)))
    }
    addSchema(2700, SAME_NAMESPACED).apply {
        addFixer(RenameBlockWithJigsawFix(this, "Renamed cave vines blocks", rename(mapOf(
            "minecraft:cave_vines_head" to "minecraft:cave_vines",
            "minecraft:cave_vines_body" to "minecraft:cave_vines_plant"
        ))))
    }
    addSchema(2701, SAME_NAMESPACED).apply {
        addFixer(SavedDataPoolElementFeatureFix(this))
    }
    addSchema(2702, SAME_NAMESPACED).apply {
        addFixer(PickupArrowFix(this))
    }
    addSchema(2704, ::V2704).apply {
        addFixer(AddChoices(this, "Added Goat", References.ENTITY))
    }
    addSchema(2707, ::V2707).apply {
        addFixer(AddChoices(this, "Added Marker", References.ENTITY))
    }
    addSchema(2710, SAME_NAMESPACED).apply {
        addFixer(RenameStatsFix(this, "Renamed play_one_minute stat to play_time", mapOf("minecraft:play_one_minute" to "minecraft:play_time")))
    }
    addSchema(2717, SAME_NAMESPACED).apply {
        addFixer(RenameItemFix(this, "Rename azalea_leaves_flowers", rename(mapOf("minecraft:azalea_leaves_flowers" to "minecraft:flowering_azalea_leaves"))))
        addFixer(RenameBlockFix(this, "Rename azalea_leaves_flowers items", rename(mapOf("minecraft:azalea_leaves_flowers" to "minecraft:flowering_azalea_leaves"))))
    }
}.build(BOOTSTRAP_EXECUTOR)

private fun rename(pair: Pair<String, String>): (String) -> String = { if (it == pair.first) pair.second else it }

private fun rename(map: Map<String, String>): (String) -> String = { map.getOrDefault(it, it) }

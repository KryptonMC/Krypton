package me.bristermitten.minekraft.registry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Registries(
    @SerialName("minecraft:sound_event") val sounds: Registry,
    @SerialName("minecraft:fluid") val fluids: DefaultedRegistry,
    @SerialName("minecraft:mob_effect") val mobEffects: Registry,
    @SerialName("minecraft:block") val blocks: DefaultedRegistry,
    @SerialName("minecraft:enchantment") val enchantments: Registry,
    @SerialName("minecraft:entity_type") val entityTypes: DefaultedRegistry,
    @SerialName("minecraft:item") val items: DefaultedRegistry,
    @SerialName("minecraft:potion") val potions: DefaultedRegistry,
    @SerialName("minecraft:particle_type") val particles: Registry,
    @SerialName("minecraft:block_entity_type") val blockEntities: Registry,
    @SerialName("minecraft:motive") val motives: DefaultedRegistry,
    @SerialName("minecraft:custom_stat") val statistics: Registry,
    @SerialName("minecraft:chunk_status") val chunkStatuses: DefaultedRegistry,
    @SerialName("minecraft:rule_test") val ruleTests: Registry,
    @SerialName("minecraft:pos_rule_test") val positiveRuleTests: Registry,
    @SerialName("minecraft:menu") val menus: Registry,
    @SerialName("minecraft:recipe_type") val recipeTypes: Registry,
    @SerialName("minecraft:recipe_serializer") val recipeSerializer: Registry,
    @SerialName("minecraft:attribute") val attributes: Registry,
    @SerialName("minecraft:stat_type") val statisticTypes: Registry,
    @SerialName("minecraft:villager_type") val villagerTypes: DefaultedRegistry,
    @SerialName("minecraft:villager_profession") val villagerProfessions: DefaultedRegistry,
    @SerialName("minecraft:point_of_interest_type") val villagerPointOfInterestTypes: DefaultedRegistry,
    @SerialName("minecraft:memory_module_type") val villagerMemoryModuleTypes: DefaultedRegistry,
    @SerialName("minecraft:sensor_type") val sensorTypes: DefaultedRegistry,
    @SerialName("minecraft:schedule") val villagerSchedules: Registry,
    @SerialName("minecraft:activity") val activities: Registry,
    @SerialName("minecraft:loot_pool_entry_type") val lootPoolEntryTypes: Registry,
    @SerialName("minecraft:loot_function_type") val lootFunctionTypes: Registry,
    @SerialName("minecraft:loot_condition_type") val lootConditionTypes: Registry,
    @SerialName("minecraft:worldgen/surface_builder") val worldGenSurfaceBuilders: Registry,
    @SerialName("minecraft:worldgen/carver") val worldGenCarvers: Registry,
    @SerialName("minecraft:worldgen/feature") val worldGenFeatures: Registry,
    @SerialName("minecraft:worldgen/structure_feature") val worldGenStructureFeatures: Registry,
    @SerialName("minecraft:worldgen/structure_piece") val worldGenStructurePieces: Registry,
    @SerialName("minecraft:worldgen/decorator") val worldGenDecorators: Registry,
    @SerialName("minecraft:worldgen/block_state_provider_type") val worldGenBlockStateProviderTypes: Registry,
    @SerialName("minecraft:worldgen/block_placer_type") val worldGenBlockPlacerTypes: Registry,
    @SerialName("minecraft:worldgen/foliage_placer_type") val worldGenFoliagePlacerTypes: Registry,
    @SerialName("minecraft:worldgen/trunk_placer_type") val worldGenTrunkPlacerTypes: Registry,
    @SerialName("minecraft:worldgen/tree_decorator_type") val worldGenTreeDecoratorTypes: Registry,
    @SerialName("minecraft:worldgen/feature_size_type") val worldGenFeatureSizeTypes: Registry,
    @SerialName("minecraft:worldgen/biome_source") val worldGenBiomeSources: Registry,
    @SerialName("minecraft:worldgen/chunk_generator") val worldGenChunkGenerators: Registry,
    @SerialName("minecraft:worldgen/structure_processor") val worldGenStructureProcessors: Registry,
    @SerialName("minecraft:worldgen/structure_pool_element") val worldGenStructurePoolElements: Registry
)

@Serializable
data class Registry(
    @SerialName("protocol_id") val protocolId: Int,
    val entries: Map<NamespacedKey, RegistryEntry>
)

@Serializable
data class DefaultedRegistry(
    val default: String,
    @SerialName("protocol_id") val protocolId: Int,
    val entries: Map<NamespacedKey, RegistryEntry>
)

@Serializable
data class RegistryEntry(
    @SerialName("protocol_id") val protocolId: Int
)

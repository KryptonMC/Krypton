/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.data

import net.kyori.adventure.text.Component
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.PushReaction
import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.animal.type.AxolotlVariant
import org.kryptonmc.api.entity.animal.type.CatVariant
import org.kryptonmc.api.entity.animal.type.FoxType
import org.kryptonmc.api.entity.animal.type.MooshroomType
import org.kryptonmc.api.entity.animal.type.PandaGene
import org.kryptonmc.api.entity.animal.type.ParrotType
import org.kryptonmc.api.entity.animal.type.RabbitType
import org.kryptonmc.api.entity.aquatic.TropicalFishVariant
import org.kryptonmc.api.entity.hanging.Picture
import org.kryptonmc.api.entity.player.ChatVisibility
import org.kryptonmc.api.entity.projectile.ArrowLike
import org.kryptonmc.api.entity.projectile.FishingHook
import org.kryptonmc.api.entity.vehicle.BoatType
import org.kryptonmc.api.entity.vehicle.MinecartType
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.data.WrittenBookGeneration
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3f
import org.spongepowered.math.vector.Vector3i
import java.awt.Color
import java.time.Instant
import java.util.Locale
import java.util.UUID

/**
 * All of the keys for every data holder in the API.
 */
@Catalogue(Key::class)
public object Keys {

    // Block keys
    @JvmField
    public val HARDNESS: Key<Double> = of("hardness")
    @JvmField
    public val EXPLOSION_RESISTANCE: Key<Double> = of("explosion_resistance")
    @JvmField
    public val FRICTION: Key<Double> = of("friction")
    @JvmField
    public val IS_SOLID: Key<Boolean> = of("is_solid")
    @JvmField
    public val IS_LIQUID: Key<Boolean> = of("is_liquid")
    @JvmField
    public val IS_FLAMMABLE: Key<Boolean> = of("is_flammable")
    @JvmField
    public val IS_REPLACEABLE: Key<Boolean> = of("is_replaceable")
    @JvmField
    public val IS_OPAQUE: Key<Boolean> = of("is_opaque")
    @JvmField
    public val BLOCKS_MOTION: Key<Boolean> = of("blocks_motion")
    @JvmField
    public val HAS_GRAVITY: Key<Boolean> = of("has_gravity")
    @JvmField
    public val CAN_RESPAWN_IN: Key<Boolean> = of("can_respawn_in")
    @JvmField
    public val PUSH_REACTION: Key<PushReaction> = of("push_reaction")
    @JvmField
    public val HAS_BLOCK_ENTITY: Key<Boolean> = of("has_block_entity")

    // Entity (base) keys
    @JvmField
    public val CUSTOM_NAME: Key<Component> = of("custom_name")
    @JvmField
    public val IS_CUSTOM_NAME_VISIBLE: Key<Boolean> = of("is_custom_name_visible")
    @JvmField
    public val DISPLAY_NAME: Key<Component> = of("display_name")
    @JvmField
    public val LOCATION: Key<Vector3d> = of("location")
    @JvmField
    public val ROTATION: Key<Vector2f> = of("rotation")
    @JvmField
    public val VELOCITY: Key<Vector3d> = of("velocity")
    @JvmField
    public val PASSENGERS: Key<List<Entity>> = of("passengers")
    @JvmField
    public val VEHICLE: Key<Entity> = of("vehicle")
    @JvmField
    public val IS_INVULNERABLE: Key<Boolean> = of("is_invulnerable")
    @JvmField
    public val IS_ON_FIRE: Key<Boolean> = of("is_on_fire")
    @JvmField
    public val IS_ON_GROUND: Key<Boolean> = of("is_on_ground")
    @JvmField
    public val IS_SNEAKING: Key<Boolean> = of("is_sneaking")
    @JvmField
    public val IS_SPRINTING: Key<Boolean> = of("is_sprinting")
    @JvmField
    public val IS_SWIMMING: Key<Boolean> = of("is_swimming")
    @JvmField
    public val IS_INVISIBLE: Key<Boolean> = of("is_invisible")
    @JvmField
    public val IS_GLOWING: Key<Boolean> = of("is_glowing")
    @JvmField
    public val IS_GLIDING: Key<Boolean> = of("is_gliding")
    @JvmField
    public val IS_SILENT: Key<Boolean> = of("is_silent")
    @JvmField
    public val TICKS_EXISTED: Key<Int> = of("ticks_existed")
    @JvmField
    public val AIR: Key<Int> = of("air")
    @JvmField
    public val FIRE_TICKS: Key<Int> = of("fire_ticks")
    @JvmField
    public val FROZEN_TICKS: Key<Int> = of("frozen_ticks")
    @JvmField
    public val FALL_DISTANCE: Key<Float> = of("fall_distance")
    @JvmField
    public val IN_WATER: Key<Boolean> = of("in_water")
    @JvmField
    public val IN_LAVA: Key<Boolean> = of("in_lava")
    @JvmField
    public val UNDERWATER: Key<Boolean> = of("underwater")

    // Ageable keys
    @JvmField
    public val AGE: Key<Int> = of("age")

    // Area effect cloud keys
    @JvmField
    public val DURATION: Key<Int> = of("duration")
    @JvmField
    public val RADIUS: Key<Float> = of("radius")

    // Armor stand keys
    @JvmField
    public val IS_SMALL: Key<Boolean> = of("is_small")
    @JvmField
    public val HAS_ARMS: Key<Boolean> = of("has_arms")
    @JvmField
    public val HAS_BASE_PLATE: Key<Boolean> = of("has_base_plate")
    @JvmField
    public val IS_MARKER: Key<Boolean> = of("is_marker")
    @JvmField
    public val HEAD_POSE: Key<Vector3f> = of("head_pose")
    @JvmField
    public val BODY_POSE: Key<Vector3f> = of("body_pose")
    @JvmField
    public val LEFT_ARM_POSE: Key<Vector3f> = of("left_arm_pose")
    @JvmField
    public val RIGHT_ARM_POSE: Key<Vector3f> = of("right_arm_pose")
    @JvmField
    public val LEFT_LEG_POSE: Key<Vector3f> = of("left_leg_pose")
    @JvmField
    public val RIGHT_LEG_POSE: Key<Vector3f> = of("right_leg_pose")

    // Experience orb keys
    @JvmField
    public val EXPERIENCE: Key<Int> = of("experience")

    // Living entity keys
    @JvmField
    public val HEALTH: Key<Float> = of("health")
    @JvmField
    public val MAX_HEALTH: Key<Float> = of("max_health")
    @JvmField
    public val ABSORPTION: Key<Float> = of("absorption")
    @JvmField
    public val IS_USING_ITEM: Key<Boolean> = of("is_using_item")
    @JvmField
    public val HAND: Key<Hand> = of("hand")
    @JvmField
    public val IS_IN_RIPTIDE_SPIN_ATTACK: Key<Boolean> = of("is_in_riptide_spin_attack")
    @JvmField
    public val LAST_HURT_TIMESTAMP: Key<Int> = of("last_hurt_timestamp")
    @JvmField
    public val SLEEPING_POSITION: Key<Vector3i> = of("sleeping_position")

    // Mob keys
    @JvmField
    public val IS_PERSISTENT: Key<Boolean> = of("is_persistent")
    @JvmField
    public val HAS_AI: Key<Boolean> = of("has_ai")
    @JvmField
    public val IS_AGGRESSIVE: Key<Boolean> = of("is_aggressive")
    @JvmField
    public val MAIN_HAND: Key<MainHand> = of("main_hand")

    // Bat keys
    @JvmField
    public val IS_RESTING: Key<Boolean> = of("is_resting")

    // Animal keys
    @JvmField
    public val IN_LOVE_TIME: Key<Int> = of("in_love_time")
    @JvmField
    public val LOVE_CAUSE: Key<UUID> = of("love_cause")

    // Axolotl keys
    @JvmField
    public val AXOLOTL_VARIANT: Key<AxolotlVariant> = of("axolotl_variant")
    @JvmField
    public val IS_PLAYING_DEAD: Key<Boolean> = of("is_playing_dead")

    // Bee keys
    @JvmField
    public val IS_ANGRY: Key<Boolean> = of("is_angry")
    @JvmField
    public val HAS_STUNG: Key<Boolean> = of("has_stung")
    @JvmField
    public val HAS_NECTAR: Key<Boolean> = of("has_nectar")
    @JvmField
    public val CANNOT_ENTER_HIVE_TICKS: Key<Int> = of("cannot_enter_hive_ticks")
    @JvmField
    public val HIVE: Key<Vector3i> = of("hive")
    @JvmField
    public val FLOWER: Key<Vector3i> = of("flower")

    // Cat keys
    @JvmField
    public val CAT_VARIANT: Key<CatVariant> = of("cat_variant")
    @JvmField
    public val IS_LYING: Key<Boolean> = of("is_lying")
    @JvmField
    public val IS_RELAXED: Key<Boolean> = of("is_relaxed")
    @JvmField
    public val COLLAR_COLOR: Key<DyeColor> = of("collar_color")

    // Chicken keys
    @JvmField
    public val IS_JOCKEY: Key<Boolean> = of("is_jockey")
    @JvmField
    public val EGG_COOLDOWN_TIME: Key<Int> = of("egg_cooldown_time")

    // Fox keys
    @JvmField
    public val FOX_TYPE: Key<FoxType> = of("fox_type")
    @JvmField
    public val IS_SITTING: Key<Boolean> = of("is_sitting")
    @JvmField
    public val IS_CROUCHING: Key<Boolean> = of("is_crouching")
    @JvmField
    public val IS_INTERESTED: Key<Boolean> = of("is_interested")
    @JvmField
    public val IS_POUNCING: Key<Boolean> = of("is_pouncing")
    @JvmField
    public val IS_SLEEPING: Key<Boolean> = of("is_sleeping")
    @JvmField
    public val HAS_FACEPLANTED: Key<Boolean> = of("has_faceplanted")
    @JvmField
    public val IS_DEFENDING: Key<Boolean> = of("is_defending")
    @JvmField
    public val FIRST_TRUSTED: Key<UUID> = of("first_trusted")
    @JvmField
    public val SECOND_TRUSTED: Key<UUID> = of("second_trusted")

    // Goat keys
    @JvmField
    public val CAN_SCREAM: Key<Boolean> = of("can_scream")

    // Mooshroom keys
    @JvmField
    public val MOOSHROOM_TYPE: Key<MooshroomType> = of("mooshroom_type")

    // Ocelot keys
    @JvmField
    public val IS_TRUSTING: Key<Boolean> = of("is_trusting")

    // Panda keys
    @JvmField
    public val KNOWN_GENE: Key<PandaGene> = of("known_gene")
    @JvmField
    public val HIDDEN_GENE: Key<PandaGene> = of("hidden_gene")
    @JvmField
    public val IS_SNEEZING: Key<Boolean> = of("is_sneezing")
    @JvmField
    public val IS_EATING: Key<Boolean> = of("is_eating")
    @JvmField
    public val IS_ROLLING: Key<Boolean> = of("is_rolling")
    @JvmField
    public val IS_LYING_ON_BACK: Key<Boolean> = of("is_lying_on_back")
    @JvmField
    public val IS_SCARED: Key<Boolean> = of("is_scared")
    @JvmField
    public val UNHAPPY_TIME: Key<Int> = of("unhappy_time")
    @JvmField
    public val SNEEZING_TIME: Key<Int> = of("sneezing_time")
    @JvmField
    public val EATING_TIME: Key<Int> = of("eating_time")

    // Parrot keys
    @JvmField
    public val PARROT_TYPE: Key<ParrotType> = of("parrot_type")

    // Pig keys
    @JvmField
    public val IS_SADDLED: Key<Boolean> = of("is_saddled")

    // Polar bear keys
    @JvmField
    public val IS_STANDING: Key<Boolean> = of("is_standing")

    // Rabbit keys
    @JvmField
    public val RABBIT_TYPE: Key<RabbitType> = of("rabbit_type")

    // Sheep keys
    @JvmField
    public val IS_SHEARED: Key<Boolean> = of("is_sheared")
    @JvmField
    public val WOOL_COLOR: Key<DyeColor> = of("wool_color")

    // Tamable keys
    @JvmField
    public val IS_TAMED: Key<Boolean> = of("is_tamed")
    @JvmField
    public val TAMER: Key<UUID> = of("tamer")

    // Turtle keys
    @JvmField
    public val HAS_EGG: Key<Boolean> = of("has_egg")
    @JvmField
    public val IS_LAYING_EGG: Key<Boolean> = of("is_laying_egg")
    @JvmField
    public val IS_GOING_HOME: Key<Boolean> = of("is_going_home")
    @JvmField
    public val IS_TRAVELLING: Key<Boolean> = of("is_travelling")
    @JvmField
    public val HOME: Key<Vector3i> = of("home")
    @JvmField
    public val DESTINATION: Key<Vector3i> = of("destination")

    // Wolf keys
    @JvmField
    public val IS_BEGGING_FOR_FOOD: Key<Boolean> = of("is_begging_for_food")

    // Dolphin keys
    @JvmField
    public val TREASURE_POSITION: Key<Vector3i> = of("treasure_position")
    @JvmField
    public val HAS_FISH: Key<Boolean> = of("has_fish")
    @JvmField
    public val SKIN_MOISTURE: Key<Int> = of("skin_moisture")

    // Glow squid keys
    @JvmField
    public val REMAINING_DARK_TICKS: Key<Int> = of("remaining_dark_ticks")

    // Tropical fish keys
    @JvmField
    public val BASE_COLOR: Key<DyeColor> = of("base_color")
    @JvmField
    public val PATTERN_COLOR: Key<DyeColor> = of("dye_color")
    @JvmField
    public val TROPICAL_FISH_VARIANT: Key<TropicalFishVariant> = of("tropical_fish_variant")

    // Hanging entity keys
    @JvmField
    public val DIRECTION: Key<Direction> = of("direction")

    // Painting keys
    @JvmField
    public val PICTURE: Key<Picture> = of("picture")

    // Creeper keys
    @JvmField
    public val FUSE: Key<Int> = of("fuse")
    @JvmField
    public val EXPLOSION_RADIUS: Key<Int> = of("explosion_radius")
    @JvmField
    public val IS_PRIMED: Key<Boolean> = of("is_primed")

    // Zombie keys
    @JvmField
    public val IS_BABY: Key<Boolean> = of("is_baby")
    @JvmField
    public val IS_CONVERTING: Key<Boolean> = of("is_converting")

    // Player keys
    @JvmField
    public val FIRST_JOINED: Key<Instant> = of("first_joined")
    @JvmField
    public val LAST_JOINED: Key<Instant> = of("last_joined")
    @JvmField
    public val CAN_FLY: Key<Boolean> = of("can_fly")
    @JvmField
    public val CAN_BUILD: Key<Boolean> = of("can_build")
    @JvmField
    public val CAN_INSTANTLY_BUILD: Key<Boolean> = of("can_instantly_build")
    @JvmField
    public val IS_FLYING: Key<Boolean> = of("is_flying")
    @JvmField
    public val WALKING_SPEED: Key<Float> = of("walking_speed")
    @JvmField
    public val FLYING_SPEED: Key<Float> = of("flying_speed")
    @JvmField
    public val VIEW_DISTANCE: Key<Int> = of("view_distance")
    @JvmField
    public val CHAT_VISIBILITY: Key<ChatVisibility> = of("chat_visibility")
    @JvmField
    public val ALLOWS_LISTING: Key<Boolean> = of("allows_listing")
    @JvmField
    public val IS_VANISHED: Key<Boolean> = of("is_vanished")
    @JvmField
    public val IS_AFK: Key<Boolean> = of("is_afk")
    @JvmField
    public val GAME_MODE: Key<GameMode> = of("game_mode")
    @JvmField
    public val LOCALE: Key<Locale> = of("locale")
    @JvmField
    public val FOOD_LEVEL: Key<Int> = of("food_level")
    @JvmField
    public val EXHAUSTION: Key<Float> = of("exhaustion")
    @JvmField
    public val SATURATION: Key<Float> = of("saturation")

    // Accelerating projectile keys
    @JvmField
    public val ACCELERATION: Key<Vector3d> = of("acceleration")

    // Arrow like keys
    @JvmField
    public val IS_CRITICAL: Key<Boolean> = of("is_critical")
    @JvmField
    public val BASE_DAMAGE: Key<Double> = of("base_damage")
    @JvmField
    public val STUCK_IN_BLOCK: Key<Block> = of("stuck_in_block")
    @JvmField
    public val IS_IN_GROUND: Key<Boolean> = of("is_in_ground")
    @JvmField
    public val PIERCING_LEVEL: Key<Int> = of("piercing_level")
    @JvmField
    public val IGNORES_PHYSICS: Key<Boolean> = of("ignores_physics")
    @JvmField
    public val WAS_SHOT_FROM_CROSSBOW: Key<Boolean> = of("was_shot_from_crossbow")
    @JvmField
    public val PICKUP_STATE: Key<ArrowLike.PickupState> = of("pickup")

    // Firework rocket keys
    @JvmField
    public val LIFE: Key<Int> = of("life")
    @JvmField
    public val LIFETIME: Key<Int> = of("lifetime")
    @JvmField
    public val WAS_SHOT_AT_ANGLE: Key<Boolean> = of("was_shot_at_angle")
    @JvmField
    public val ATTACHED_ENTITY: Key<Entity> = of("attached_entity")

    // Fishing hook keys
    @JvmField
    public val HOOKED: Key<Entity> = of("hooked")
    @JvmField
    public val IS_BITING: Key<Boolean> = of("is_biting")
    @JvmField
    public val FISHING_STATE: Key<FishingHook.State> = of("fishing_state")

    // Large fireball keys
    @JvmField
    public val EXPLOSION_POWER: Key<Int> = of("explosion_power")

    // Projectile keys
    @JvmField
    public val SHOOTER: Key<Entity> = of("shooter")
    @JvmField
    public val HAS_LEFT_OWNER: Key<Boolean> = of("has_left_owner")
    @JvmField
    public val HAS_BEEN_SHOT: Key<Boolean> = of("has_been_shot")

    // Shulker bullet keys
    @JvmField
    public val STEPS: Key<Int> = of("steps")
    @JvmField
    public val TARGET: Key<Entity> = of("target")

    // Trident keys
    @JvmField
    public val DEALT_DAMAGE: Key<Boolean> = of("dealt_damage")
    @JvmField
    public val LOYALTY_LEVEL: Key<Int> = of("loyalty_level")
    @JvmField
    public val IS_ENCHANTED: Key<Boolean> = of("is_enchanted")

    // Wither skull keys
    @JvmField
    public val IS_DANGEROUS: Key<Boolean> = of("is_dangerous")

    // Boat keys
    @JvmField
    public val BOAT_TYPE: Key<BoatType> = of("boat_type")
    @JvmField
    public val IS_LEFT_PADDLE_TURNING: Key<Boolean> = of("is_left_paddle_turning")
    @JvmField
    public val IS_RIGHT_PADDLE_TURNING: Key<Boolean> = of("is_right_paddle_turning")

    // Command block minecart keys
    @JvmField
    public val COMMAND: Key<String> = of("command")
    @JvmField
    public val LAST_OUTPUT: Key<Component> = of("last_output")

    // Damageable vehicle keys
    @JvmField
    public val DAMAGE_TAKEN: Key<Float> = of("damage_taken")
    @JvmField
    public val DAMAGE_TIMER: Key<Int> = of("damage_timer")

    // Furnace minecart keys
    @JvmField
    public val HAS_FUEL: Key<Boolean> = of("has_fuel")
    @JvmField
    public val FUEL: Key<Int> = of("fuel")

    // Minecart like keys
    @JvmField
    public val MINECART_TYPE: Key<MinecartType> = of("minecart_type")
    @JvmField
    public val SHOW_CUSTOM_BLOCK: Key<Boolean> = of("show_custom_block")
    @JvmField
    public val CUSTOM_BLOCK: Key<Block> = of("custom_block")
    @JvmField
    public val CUSTOM_BLOCK_OFFSET: Key<Int> = of("custom_block_offset")

    // Item metadata keys
    @JvmField
    public val DAMAGE: Key<Int> = of("damage")
    @JvmField
    public val IS_UNBREAKABLE: Key<Boolean> = of("is_unbreakable")
    @JvmField
    public val CUSTOM_MODEL_DATA: Key<Int> = of("custom_model_data")
    @JvmField
    public val NAME: Key<Component> = of("name")
    @JvmField
    public val LORE: Key<List<Component>> = of("lore")
    @JvmField
    public val HIDE_FLAGS: Key<Int> = of("hide_flags")
    @JvmField
    public val CAN_DESTROY: Key<Set<Block>> = of("can_destroy")
    @JvmField
    public val CAN_PLACE_ON: Key<Set<Block>> = of("can_place_on")
    @JvmField
    public val BANNER_PATTERNS: Key<List<BannerPattern>> = of("banner_patterns")
    @JvmField
    public val PAGES: Key<List<Component>> = of("pages")
    @JvmField
    public val ITEMS: Key<List<ItemStack>> = of("items")
    @JvmField
    public val IS_TRACKING_LODESTONE: Key<Boolean> = of("is_tracking_lodestone")
    @JvmField
    public val LODESTONE_DIMENSION: Key<ResourceKey<World>> = of("lodestone_dimension")
    @JvmField
    public val LODESTONE_POSITION: Key<Vector3i> = of("lodestone_position")
    @JvmField
    public val IS_CHARGED: Key<Boolean> = of("is_charged")
    @JvmField
    public val PROJECTILES: Key<List<ItemStack>> = of("projectiles")
    @JvmField
    public val FIREWORK_EFFECTS: Key<List<FireworkEffect>> = of("firework_effects")
    @JvmField
    public val FLIGHT_DURATION: Key<Int> = of("flight_duration")
    @JvmField
    public val FIREWORK_EFFECT: Key<FireworkEffect> = of("firework_effect")
    @JvmField
    public val COLOR: Key<Color> = of("color")
    @JvmField
    public val HEAD_OWNER: Key<GameProfile> = of("owner")
    @JvmField
    public val TITLE: Key<Component> = of("title")
    @JvmField
    public val AUTHOR: Key<Component> = of("author")
    @JvmField
    public val GENERATION: Key<WrittenBookGeneration> = of("generation")

    @JvmStatic
    private inline fun <reified V> of(name: String): Key<V> = Key.of(net.kyori.adventure.key.Key.key("krypton", name))
}

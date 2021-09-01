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

import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.StringDataConverter
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import java.util.UUID

object V2514 {

    private const val VERSION = MCVersions.V20W11A + 1
    private val ABSTRACT_HORSES = setOf(
        "minecraft:donkey",
        "minecraft:horse",
        "minecraft:llama",
        "minecraft:mule",
        "minecraft:skeleton_horse",
        "minecraft:trader_llama",
        "minecraft:zombie_horse",
    )
    private val TAMEABLE_ANIMALS = setOf(
        "minecraft:cat",
        "minecraft:parrot",
        "minecraft:wolf",
    )
    private val ANIMALS = setOf(
        "minecraft:bee",
        "minecraft:chicken",
        "minecraft:cow",
        "minecraft:fox",
        "minecraft:mooshroom",
        "minecraft:ocelot",
        "minecraft:panda",
        "minecraft:pig",
        "minecraft:polar_bear",
        "minecraft:rabbit",
        "minecraft:sheep",
        "minecraft:turtle",
        "minecraft:hoglin",
    )
    private val MOBS = setOf(
        "minecraft:bat",
        "minecraft:blaze",
        "minecraft:cave_spider",
        "minecraft:cod",
        "minecraft:creeper",
        "minecraft:dolphin",
        "minecraft:drowned",
        "minecraft:elder_guardian",
        "minecraft:ender_dragon",
        "minecraft:enderman",
        "minecraft:endermite",
        "minecraft:evoker",
        "minecraft:ghast",
        "minecraft:giant",
        "minecraft:guardian",
        "minecraft:husk",
        "minecraft:illusioner",
        "minecraft:magma_cube",
        "minecraft:pufferfish",
        "minecraft:zombified_piglin",
        "minecraft:salmon",
        "minecraft:shulker",
        "minecraft:silverfish",
        "minecraft:skeleton",
        "minecraft:slime",
        "minecraft:snow_golem",
        "minecraft:spider",
        "minecraft:squid",
        "minecraft:stray",
        "minecraft:tropical_fish",
        "minecraft:vex",
        "minecraft:villager",
        "minecraft:iron_golem",
        "minecraft:vindicator",
        "minecraft:pillager",
        "minecraft:wandering_trader",
        "minecraft:witch",
        "minecraft:wither",
        "minecraft:wither_skeleton",
        "minecraft:zombie",
        "minecraft:zombie_villager",
        "minecraft:phantom",
        "minecraft:ravager",
        "minecraft:piglin",
    )
    private val LIVING_ENTITIES = setOf("minecraft:armor_stand")
    private val PROJECTILES = setOf(
        "minecraft:arrow",
        "minecraft:dragon_fireball",
        "minecraft:firework_rocket",
        "minecraft:fireball",
        "minecraft:llama_spit",
        "minecraft:small_fireball",
        "minecraft:snowball",
        "minecraft:spectral_arrow",
        "minecraft:egg",
        "minecraft:ender_pearl",
        "minecraft:experience_bottle",
        "minecraft:potion",
        "minecraft:trident",
        "minecraft:wither_skull",
    )

    fun register() {
        // Entity UUID fixes
        MCTypeRegistry.ENTITY.addStructureConverter(VERSION) { data, _, _ ->
            data.updateEntityUUID()
            null
        }

        val animalOwnerConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                data.updateAnimalOwner()
                return null
            }
        }
        val animalConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                data.updateAnimal()
                return null
            }
        }
        val mobConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                data.updateMob()
                return null
            }
        }
        val livingEntityConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                data.updateLivingEntity()
                return null
            }
        }
        val projectileConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                data.updateProjectile()
                return null
            }
        }
        ABSTRACT_HORSES.forEach { MCTypeRegistry.ENTITY.addConverterForId(it, animalOwnerConverter) }
        TAMEABLE_ANIMALS.forEach { MCTypeRegistry.ENTITY.addConverterForId(it, animalOwnerConverter) }
        ANIMALS.forEach { MCTypeRegistry.ENTITY.addConverterForId(it, animalConverter) }
        MOBS.forEach { MCTypeRegistry.ENTITY.addConverterForId(it, mobConverter) }
        LIVING_ENTITIES.forEach { MCTypeRegistry.ENTITY.addConverterForId(it, livingEntityConverter) }
        PROJECTILES.forEach { MCTypeRegistry.ENTITY.addConverterForId(it, projectileConverter) }

        MCTypeRegistry.ENTITY.addConverterForId("minecraft:bee", VERSION) { data, _, _ ->
            data.updateHurtBy()
            null
        }
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:zombified_piglin", VERSION) { data, _, _ ->
            data.updateHurtBy()
            null
        }
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:fox", VERSION) { data, _, _ ->
            data.updateFox()
            null
        }
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:item", VERSION) { data, _, _ ->
            data.updateItem()
            null
        }
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:shulker_bullet", VERSION) { data, _, _ ->
            data.updateShulkerBullet()
            null
        }
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:area_effect_cloud", VERSION) { data, _, _ ->
            data.updateAreaEffectCloud()
            null
        }
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:zombie_villager", VERSION) { data, _, _ ->
            data.updateZombieVillager()
            null
        }
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:evoker_fangs", VERSION) { data, _, _ ->
            data.updateEvokerFangs()
            null
        }
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:piglin", VERSION) { data, _, _ ->
            data.updatePiglin()
            null
        }

        // Tile Entity UUID fixes
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:conduit", VERSION) { data, _, _ ->
            data.replaceUUIDMLTag("target_uuid", "Target")
            null
        }
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:skull", VERSION) { data, _, _ ->
            val owner = data.getMap<String>("Owner") ?: return@addConverterForId null
            data.remove("Owner")
            owner.replaceUUIDString("Id", "Id")
            data.setMap("SkullOwner", owner)
            null
        }

        // Player UUID fix
        MCTypeRegistry.PLAYER.addStructureConverter(VERSION) { data, _, _ ->
            data.updateLivingEntity()
            data.updateEntityUUID()
            data.getMap<String>("RootVehicle")?.replaceUUIDLeastMost("Attach", "Attach")
            null
        }

        // Level.dat
        MCTypeRegistry.LEVEL.addStructureConverter(VERSION) { data, _, _ ->
            data.replaceUUIDString("WanderingTraderId", "WanderingTraderId")

            data.getMap<String>("DimensionData")?.let { dimensionData ->
                dimensionData.keys().forEach {
                    dimensionData.getMap<String>(it)!!.getMap<String>("DragonFight")
                        ?.replaceUUIDLeastMost("DragonUUID", "Dragon")
                }
            }

            data.getMap<String>("CustomBossEvents")?.let { customBossEvents ->
                customBossEvents.keys().forEach {
                    val customBossEvent = customBossEvents.getMap<String>(it)!!
                    val players = customBossEvent.getList("Players", ObjectType.MAP) ?: return@forEach

                    val newPlayers = NBTTypeUtil.createEmptyList()
                    customBossEvent.setList("Players", newPlayers)
                    for (i in 0 until players.size()) {
                        players.getMap<String>(i).createUUIDFromLongs("M", "L")?.apply { newPlayers.addIntArray(this) }
                    }
                }
            }
            null
        }
        MCTypeRegistry.SAVED_DATA.addStructureConverter(VERSION) { root, _, _ ->
            val data = root.getMap<String>("data") ?: return@addStructureConverter null
            val raids = data.getList("Raids", ObjectType.MAP) ?: return@addStructureConverter null

            for (i in 0 until raids.size()) {
                val raid = raids.getMap<String>(i)
                val heroes = raid.getList("HeroesOfTheVillage", ObjectType.MAP) ?: continue

                val newHeroes = NBTTypeUtil.createEmptyList()
                raid.setList("HeroesOfTheVillage", newHeroes)
                for (k in 0 until heroes.size()) {
                    heroes.getMap<String>(i).createUUIDFromLongs("UUIDMost", "UUIDLeast")?.apply {
                        newHeroes.addIntArray(this)
                    }
                }
            }
            null
        }
        MCTypeRegistry.ITEM_STACK.addStructureConverter(VERSION) { data, _, _ ->
            val tag = data.getMap<String>("tag") ?: return@addStructureConverter null
            tag.updateAttributeModifiers()
            if (data.getString("id") == "minecraft:player_head") tag.updateSkullOwner()
            null
        }
    }

    fun MapType<String>.replaceUUIDLeastMost(prefix: String, newPath: String) {
        val mostPath = "${prefix}Most"
        val leastPath = "${prefix}Least"
        val uuid = createUUIDFromLongs(mostPath, leastPath)
        if (uuid != null) {
            remove(mostPath)
            remove(leastPath)
            setInts(newPath, uuid)
        }
    }

    private fun createUUIDArray(most: Long, least: Long) = intArrayOf((most ushr 32).toInt(), most.toInt(), (least ushr 32).toInt(), least.toInt())

    private fun MapType<String>.createUUIDFromString(path: String): IntArray? {
        val uuidString = getString(path) ?: return null
        return try {
            val uuid = UUID.fromString(uuidString)
            createUUIDArray(uuid.mostSignificantBits, uuid.leastSignificantBits)
        } catch (ignored: IllegalArgumentException) {
            null
        }
    }

    private fun MapType<String>.createUUIDFromLongs(most: String, least: String): IntArray? {
        val mostBits = getLong(most)
        val leastBits = getLong(least)
        return if (mostBits != 0L || leastBits != 0L) createUUIDArray(mostBits, leastBits) else null
    }

    private fun MapType<String>.replaceUUIDString(oldPath: String, newPath: String) {
        val newUUID = createUUIDFromString(oldPath)
        if (newUUID != null) {
            remove(oldPath)
            setInts(newPath, newUUID)
        }
    }

    private fun MapType<String>.replaceUUIDMLTag(oldPath: String, newPath: String) {
        val uuid = getMap<String>(oldPath)?.createUUIDFromLongs("M", "L")
        if (uuid != null) {
            remove(oldPath)
            setInts(newPath, uuid)
        }
    }

    private fun MapType<String>.updatePiglin() {
        val brain = getMap<String>("Brain") ?: return
        val memories = brain.getMap<String>("memories") ?: return
        memories.getMap<String>("minecraft:angry_at")?.replaceUUIDString("value", "value")
    }

    private fun MapType<String>.updateEvokerFangs() = replaceUUIDLeastMost("OwnerUUID", "Owner")

    private fun MapType<String>.updateZombieVillager() = replaceUUIDLeastMost("ConversionPlayer", "ConversionPlayer")

    private fun MapType<String>.updateAreaEffectCloud() = replaceUUIDLeastMost("OwnerUUID", "Owner")

    private fun MapType<String>.updateShulkerBullet() {
        replaceUUIDMLTag("Owner", "Owner")
        replaceUUIDMLTag("Target", "Target")
    }

    private fun MapType<String>.updateItem() {
        replaceUUIDMLTag("Owner", "Owner")
        replaceUUIDMLTag("Thrower", "Thrower")
    }

    private fun MapType<String>.updateFox() {
        val trustedUUIDs = getList("TrustedUUIDs", ObjectType.MAP) ?: return
        val newUUIDs = NBTTypeUtil.createEmptyList()
        remove("TrustedUUIDs")
        setList("Trusted", newUUIDs)
        for (i in 0 until trustedUUIDs.size()) {
            trustedUUIDs.getMap<String>(i).createUUIDFromLongs("M", "L")?.apply { newUUIDs.addIntArray(this) }
        }
    }

    private fun MapType<String>.updateHurtBy() = replaceUUIDString("HurtBy", "HurtBy")

    private fun MapType<String>.updateAnimalOwner() {
        updateAnimal()
        replaceUUIDString("OwnerUUID", "Owner")
    }

    private fun MapType<String>.updateAnimal() {
        updateMob()
        replaceUUIDLeastMost("LoveCause", "LoveCause")
    }

    private fun MapType<String>.updateMob() {
        updateLivingEntity()
        getMap<String>("Leash")?.replaceUUIDLeastMost("UUID", "UUID")
    }

    private fun MapType<String>.updateLivingEntity() {
        val attributes = getList("Attributes", ObjectType.MAP) ?: return
        for (i in 0 until attributes.size()) {
            val attribute = attributes.getMap<String>(i)
            val modifiers = attribute.getList("Modifiers", ObjectType.MAP) ?: continue
            for (k in 0 until modifiers.size()) {
                modifiers.getMap<String>(k).replaceUUIDLeastMost("UUID", "UUID")
            }
        }
    }

    private fun MapType<String>.updateProjectile() {
        val ownerUUID = getGeneric("OwnerUUID")
        if (ownerUUID != null) {
            remove("OwnerUUID")
            setGeneric("Owner", ownerUUID)
        }
    }

    private fun MapType<String>.updateEntityUUID() = replaceUUIDLeastMost("UUID", "UUID")

    private fun MapType<String>.updateAttributeModifiers() {
        val attributes = getList("AttributeModifiers", ObjectType.MAP) ?: return
        for (i in 0 until attributes.size()) {
            attributes.getMap<String>(i).replaceUUIDLeastMost("UUID", "UUID")
        }
    }

    private fun MapType<String>.updateSkullOwner() {
        getMap<String>("SkullOwner")?.replaceUUIDString("Id", "Id")
    }
}

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
package org.kryptonmc.krypton.util.datafix.fixes.uuid

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class EntityUUIDFix(outputSchema: Schema) : UUIDFix(outputSchema, References.ENTITY) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped("EntityUUIDFixes", inputSchema.getType(typeReference)) { typed ->
        var temp = typed.update(remainderFinder()) { it.updateEntityUUID() }
        HORSES.forEach { horse -> temp = temp.updateNamedChoice(horse) { it.updateAnimalOwner() } }
        TAMEABLE_ANIMALS.forEach { tameable -> temp = temp.updateNamedChoice(tameable) { it.updateAnimalOwner() } }
        ANIMALS.forEach { animal -> temp = temp.updateNamedChoice(animal) { it.updateAnimal() } }
        MOBS.forEach { mob -> temp = temp.updateNamedChoice(mob) { it.updateMob() } }
        LIVING_ENTITIES.forEach { living -> temp = temp.updateNamedChoice(living) { it.updateLivingEntity() } }
        PROJECTILES.forEach { projectile -> temp = temp.updateNamedChoice(projectile) { it.updateProjectile() } }
        temp = temp.updateNamedChoice("minecraft:bee") { it.updateHurtBy() }
        temp = temp.updateNamedChoice("minecraft:zombified_piglin") { it.updateHurtBy() }
        temp = temp.updateNamedChoice("minecraft:fox") { it.updateFox() }
        temp = temp.updateNamedChoice("minecraft:item") { it.updateItem() }
        temp = temp.updateNamedChoice("minecraft:shulker_bullet") { it.updateShulkerBullet() }
        temp = temp.updateNamedChoice("minecraft:area_effect_cloud") { it.updateAreaEffectCloud() }
        temp = temp.updateNamedChoice("minecraft:zombie_villager") { it.updateZombieVillager() }
        temp = temp.updateNamedChoice("minecraft:evoker_fangs") { it.updateEvokerFangs() }
        temp.updateNamedChoice("minecraft:piglin") { it.updatePiglin() }
    }

    private fun Dynamic<*>.updatePiglin() = update("Brain") { brain -> brain.update("memories") { memories -> memories.update("minecraft:angry_at") { it.replaceUUIDString("value", "value").orElse(it) } } }

    private fun Dynamic<*>.updateEvokerFangs() = replaceUUIDLeastMost("OwnerUUID", "Owner").orElse(this)

    private fun Dynamic<*>.updateZombieVillager() = replaceUUIDLeastMost("ConversionPlayer", "ConversionPlayer").orElse(this)

    private fun Dynamic<*>.updateAreaEffectCloud() = replaceUUIDLeastMost("OwnerUUID", "Owner").orElse(this)

    private fun Dynamic<*>.updateShulkerBullet(): Dynamic<*> {
        val temp = replaceUUIDMLTag("Owner", "Owner").orElse(this)
        return temp.replaceUUIDMLTag("Target", "Target").orElse(temp)
    }

    private fun Dynamic<*>.updateItem(): Dynamic<*> {
        val temp = replaceUUIDMLTag("Owner", "Owner").orElse(this)
        return temp.replaceUUIDMLTag("Thrower", "Thrower").orElse(temp)
    }

    private fun Dynamic<*>.updateFox(): Dynamic<*> {
        val trustedUUIDs = get("TrustedUUIDs").result().map { trusted ->
            createList(trusted.asStream().map { uuid -> uuid.createUUIDFromML().orElse(uuid) })
        }
        return trustedUUIDs.map { remove("TrustedUUIDs").set("Trusted", it) }.orElse(this)
    }

    private fun Dynamic<*>.updateHurtBy() = replaceUUIDString("HurtBy", "HurtBy").orElse(this)

    private fun Dynamic<*>.updateAnimalOwner(): Dynamic<*> {
        val updated = updateAnimal()
        return updated.replaceUUIDString("OwnerUUID", "Owner").orElse(updated)
    }

    private fun Dynamic<*>.updateAnimal(): Dynamic<*> {
        val updated = updateMob()
        return updated.replaceUUIDLeastMost("LoveCause", "LoveCause").orElse(updated)
    }

    private fun Dynamic<*>.updateMob() = updateLivingEntity().update("Leash") { it.replaceUUIDLeastMost("UUID", "UUID").orElse(it) }

    private fun Dynamic<*>.updateProjectile() = get("OwnerUUID").result().map { remove("OwnerUUID").set("Owner", it) }.orElse(this)

    companion object {

        private val HORSES = setOf(
            "minecraft:donkey",
            "minecraft:horse",
            "minecraft:llama",
            "minecraft:mule",
            "minecraft:skeleton_horse",
            "minecraft:trader_llama",
            "minecraft:zombie_horse"
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
            "minecraft:hoglin"
        )
        private val TAMEABLE_ANIMALS = setOf(
            "minecraft:cat",
            "minecraft:parrot",
            "minecraft:wolf"
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
            "minecraft:wither_skull"
        )
    }
}

fun Dynamic<*>.updateEntityUUID(): Dynamic<*> = replaceUUIDLeastMost("UUID", "UUID").orElse(this)

fun Dynamic<*>.updateLivingEntity(): Dynamic<*> = update("Attributes") { attributes ->
    createList(attributes.asStream().map { instance ->
        instance.update("Modifiers") { modifiers ->
            instance.createList(modifiers.asStream().map { it.replaceUUIDLeastMost("UUID", "UUID").orElse(it) })
        }
    })
}

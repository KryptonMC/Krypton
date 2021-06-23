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
package org.kryptonmc.krypton.util.datafix.fixes.item

import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DSL.string
import com.mojang.datafixers.DSL.typeFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema

class SpawnEggItemFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val itemType = inputSchema.getType(References.ITEM_STACK)
        val idFinder = fieldFinder("id", named(References.ITEM_NAME.typeName(), NamespacedSchema.NAMESPACED_STRING))
        val tagFinder = itemType.findField("tag")
        val entityTagFinder = tagFinder.type().findField("EntityTag")
        val entityFinder = typeFinder(inputSchema.getTypeRaw(References.ENTITY))
        val entityType = outputSchema.getTypeRaw(References.ENTITY)
        return fixTypeEverywhereTyped("SpawnEggFix", itemType) { typed ->
            typed.getOptional(idFinder).takeIf { it.isPresent }?.get() ?: return@fixTypeEverywhereTyped typed
            var data = typed[remainderFinder()]
            val damage = data["Damage"].asShort(0)
            val optionalTagTyped = typed.getOptionalTyped(tagFinder)
            val optionalEntityTagTyped = optionalTagTyped.flatMap { it.getOptionalTyped(entityTagFinder) }
            val optionalEntityTyped = optionalEntityTagTyped.flatMap { it.getOptionalTyped(entityFinder) }
            val id = optionalEntityTyped.flatMap { it.getOptional(fieldFinder("id", string())) }
            val remapped = ID_TO_ENTITY[damage.toInt() and 255]
            var temp = typed
            if (!id.isPresent || id.get() != remapped) {
                val tagTyped = typed.getOrCreateTyped(tagFinder)
                val entityTagTyped = tagTyped.getOrCreateTyped(entityTagFinder)
                val entityTyped = entityTagTyped.getOrCreateTyped(entityFinder)
                val id1 = entityTyped.write().flatMap { entityType.readTyped(it.set("id", data.createString(remapped))) }.result().orElseThrow { IllegalStateException("Could not parse new entity") }.first
                temp = typed.set(tagFinder, tagTyped.set(entityTagFinder, entityTagTyped.set(entityFinder, id1)))
            }
            if (damage.toInt() != 0) {
                data = data.set("Damage", data.createShort(0.toShort()))
                temp = temp.set(remainderFinder(), data)
            }
            temp
        }
    }

    companion object {

        private val ID_TO_ENTITY = arrayOf(
            "Item",
            "XPOrb",
            "ThrownEgg",
            "LeashKnot",
            "Painting",
            "Arrow",
            "Snowball",
            "Fireball",
            "SmallFireball",
            "ThrownEnderpearl",
            "EyeOfEnderSignal",
            "ThrownPotion",
            "ThrownExpBottle",
            "ItemFrame",
            "WitherSkull",
            "PrimedTnt",
            "FallingSand",
            "FireworksRocketEntity",
            "TippedArrow",
            "SpectralArrow",
            "ShulkerBullet",
            "DragonFireball",
            "ArmorStand",
            "Boat",
            "MinecartRideable",
            "MinecartChest",
            "MinecartFurnace",
            "MinecartTNT",
            "MinecartHopper",
            "MinecartSpawner",
            "MinecartCommandBlock",
            "Mob",
            "Monster",
            "Creeper",
            "Skeleton",
            "Spider",
            "Giant",
            "Zombie",
            "Slime",
            "Ghast",
            "PigZombie",
            "Enderman",
            "CaveSpider",
            "Silverfish",
            "Blaze",
            "LavaSlime",
            "EnderDragon",
            "WitherBoss",
            "Bat",
            "Witch",
            "Endermite",
            "Guardian",
            "Shulker",
            "Pig",
            "Sheep",
            "Cow",
            "Chicken",
            "Squid",
            "Wolf",
            "MushroomCow",
            "SnowMan",
            "Ozelot",
            "VillagerGolem",
            "EntityHorse",
            "Rabbit",
            "Villager",
            "EnderCrystal"
        )
    }
}

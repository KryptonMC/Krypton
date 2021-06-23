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
package org.kryptonmc.krypton.util.datafix.fixes.entity

import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Function

class EntityIdFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    @Suppress("UNCHECKED_CAST")
    override fun makeRule(): TypeRewriteRule {
        val inputChoiceType = inputSchema.findChoiceType(References.ENTITY) as TaggedChoiceType<String>
        val outputChoiceType = outputSchema.findChoiceType(References.ENTITY) as TaggedChoiceType<String>
        val inputItemType = inputSchema.getType(References.ITEM_STACK)
        val outputItemType = outputSchema.getType(References.ITEM_STACK)
        return TypeRewriteRule.seq(
            convertUnchecked("Item stack entity name hook converter", inputItemType, outputItemType),
            fixTypeEverywhere("EntityIdFix", inputChoiceType, outputChoiceType) { Function { pair -> pair.mapFirst { REMAPPED_IDS.getOrDefault(it, it) } } }
        )
    }

    companion object {

        private val REMAPPED_IDS = mapOf(
            "AreaEffectCloud" to "minecraft:area_effect_cloud",
            "ArmorStand" to "minecraft:armor_stand",
            "Arrow" to "minecraft:arrow",
            "Bat" to "minecraft:bat",
            "Blaze" to "minecraft:blaze",
            "Boat" to "minecraft:boat",
            "CaveSpider" to "minecraft:cave_spider",
            "Chicken" to "minecraft:chicken",
            "Cow" to "minecraft:cow",
            "Creeper" to "minecraft:creeper",
            "Donkey" to "minecraft:donkey",
            "DragonFireball" to "minecraft:dragon_fireball",
            "ElderGuardian" to "minecraft:elder_guardian",
            "EnderCrystal" to "minecraft:ender_crystal",
            "EnderDragon" to "minecraft:ender_dragon",
            "Enderman" to "minecraft:enderman",
            "Endermite" to "minecraft:endermite",
            "EyeOfEnderSignal" to "minecraft:eye_of_ender_signal",
            "FallingSand" to "minecraft:falling_block",
            "Fireball" to "minecraft:fireball",
            "FireworksRocketEntity" to "minecraft:fireworks_rocket",
            "Ghast" to "minecraft:ghast",
            "Giant" to "minecraft:giant",
            "Guardian" to "minecraft:guardian",
            "Horse" to "minecraft:horse",
            "Husk" to "minecraft:husk",
            "Item" to "minecraft:item",
            "ItemFrame" to "minecraft:item_frame",
            "LavaSlime" to "minecraft:magma_cube",
            "LeashKnot" to "minecraft:leash_knot",
            "MinecartChest" to "minecraft:chest_minecart",
            "MinecartCommandBlock" to "minecraft:commandblock_minecart",
            "MinecartFurnace" to "minecraft:furnace_minecart",
            "MinecartHopper" to "minecraft:hopper_minecart",
            "MinecartRideable" to "minecraft:minecart",
            "MinecartSpawner" to "minecraft:spawner_minecart",
            "MinecartTNT" to "minecraft:tnt_minecart",
            "Mule" to "minecraft:mule",
            "MushroomCow" to "minecraft:mooshroom",
            "Ozelot" to "minecraft:ocelot",
            "Painting" to "minecraft:painting",
            "Pig" to "minecraft:pig",
            "PigZombie" to "minecraft:zombie_pigman",
            "PolarBear" to "minecraft:polar_bear",
            "PrimedTnt" to "minecraft:tnt",
            "Rabbit" to "minecraft:rabbit",
            "Sheep" to "minecraft:sheep",
            "Shulker" to "minecraft:shulker",
            "ShulkerBullet" to "minecraft:shulker_bullet",
            "Silverfish" to "minecraft:silverfish",
            "Skeleton" to "minecraft:skeleton",
            "SkeletonHorse" to "minecraft:skeleton_horse",
            "Slime" to "minecraft:slime",
            "SmallFireball" to "minecraft:small_fireball",
            "SnowMan" to "minecraft:snowman",
            "Snowball" to "minecraft:snowball",
            "SpectralArrow" to "minecraft:spectral_arrow",
            "Spider" to "minecraft:spider",
            "Squid" to "minecraft:squid",
            "Stray" to "minecraft:stray",
            "ThrownEgg" to "minecraft:egg",
            "ThrownEnderpearl" to "minecraft:ender_pearl",
            "ThrownExpBottle" to "minecraft:xp_bottle",
            "ThrownPotion" to "minecraft:potion",
            "Villager" to "minecraft:villager",
            "VillagerGolem" to "minecraft:villager_golem",
            "Witch" to "minecraft:witch",
            "WitherBoss" to "minecraft:wither",
            "WitherSkeleton" to "minecraft:wither_skeleton",
            "WitherSkull" to "minecraft:wither_skull",
            "Wolf" to "minecraft:wolf",
            "XPOrb" to "minecraft:xp_orb",
            "Zombie" to "minecraft:zombie",
            "ZombieHorse" to "minecraft:zombie_horse",
            "ZombieVillager" to "minecraft:zombie_villager"
        )
    }
}

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
package org.kryptonmc.krypton.util.converters.helpers

object SpawnEggNamingHelper {

    private val ID_TO_STRING = arrayOfNulls<String>(256).apply {
        this[1] = "Item"
        this[2] = "XPOrb"
        this[7] = "ThrownEgg"
        this[8] = "LeashKnot"
        this[9] = "Painting"
        this[10] = "Arrow"
        this[11] = "Snowball"
        this[12] = "Fireball"
        this[13] = "SmallFireball"
        this[14] = "ThrownEnderpearl"
        this[15] = "EyeOfEnderSignal"
        this[16] = "ThrownPotion"
        this[17] = "ThrownExpBottle"
        this[18] = "ItemFrame"
        this[19] = "WitherSkull"
        this[20] = "PrimedTnt"
        this[21] = "FallingSand"
        this[22] = "FireworksRocketEntity"
        this[23] = "TippedArrow"
        this[24] = "SpectralArrow"
        this[25] = "ShulkerBullet"
        this[26] = "DragonFireball"
        this[30] = "ArmorStand"
        this[41] = "Boat"
        this[42] = "MinecartRideable"
        this[43] = "MinecartChest"
        this[44] = "MinecartFurnace"
        this[45] = "MinecartTNT"
        this[46] = "MinecartHopper"
        this[47] = "MinecartSpawner"
        this[40] = "MinecartCommandBlock"
        this[48] = "Mob"
        this[49] = "Monster"
        this[50] = "Creeper"
        this[51] = "Skeleton"
        this[52] = "Spider"
        this[53] = "Giant"
        this[54] = "Zombie"
        this[55] = "Slime"
        this[56] = "Ghast"
        this[57] = "PigZombie"
        this[58] = "Enderman"
        this[59] = "CaveSpider"
        this[60] = "Silverfish"
        this[61] = "Blaze"
        this[62] = "LavaSlime"
        this[63] = "EnderDragon"
        this[64] = "WitherBoss"
        this[65] = "Bat"
        this[66] = "Witch"
        this[67] = "Endermite"
        this[68] = "Guardian"
        this[69] = "Shulker"
        this[90] = "Pig"
        this[91] = "Sheep"
        this[92] = "Cow"
        this[93] = "Chicken"
        this[94] = "Squid"
        this[95] = "Wolf"
        this[96] = "MushroomCow"
        this[97] = "SnowMan"
        this[98] = "Ozelot"
        this[99] = "VillagerGolem"
        this[100] = "EntityHorse"
        this[101] = "Rabbit"
        this[120] = "Villager"
        this[200] = "EnderCrystal"
    }

    fun getSpawnNameFromId(id: Short) = ID_TO_STRING[id.toInt() and 255]
}

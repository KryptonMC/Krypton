/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.entity.serializer

import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.krypton.entity.KryptonEquipable
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.list

object MobSerializer : EntitySerializer<KryptonMob> {

    private const val PICKUP_LOOT_TAG = "CanPickUpLoot"
    private const val PERSISTENCE_TAG = "PersistenceRequired"
    private const val ARMOR_ITEMS_TAG = "ArmorItems"
    private const val HAND_ITEMS_TAG = "HandItems"
    private const val ARMOR_DROP_CHANCES_TAG = "ArmorDropChances"
    private const val HAND_DROP_CHANCES_TAG = "HandDropChances"
    private const val LEFT_HANDED_TAG = "LeftHanded"
    private const val NO_AI_TAG = "NoAI"

    override fun load(entity: KryptonMob, data: CompoundTag) {
        LivingEntitySerializer.load(entity, data)
        if (data.contains(PICKUP_LOOT_TAG, ByteTag.ID)) entity.canPickUpLoot = data.getBoolean(PICKUP_LOOT_TAG)
        entity.isPersistent = data.getBoolean(PERSISTENCE_TAG)

        KryptonEquipable.loadItems(data, ARMOR_ITEMS_TAG, entity.armorItems)
        KryptonEquipable.loadItems(data, HAND_ITEMS_TAG, entity.handItems)
        loadChances(data, ARMOR_DROP_CHANCES_TAG, entity.armorDropChances)
        loadChances(data, HAND_DROP_CHANCES_TAG, entity.handDropChances)

        entity.mainHand = if (data.getBoolean(LEFT_HANDED_TAG)) MainHand.LEFT else MainHand.RIGHT
        entity.hasAI = !data.getBoolean(NO_AI_TAG)
    }

    override fun save(entity: KryptonMob): CompoundTag.Builder = LivingEntitySerializer.save(entity).apply {
        putBoolean(PICKUP_LOOT_TAG, entity.canPickUpLoot)
        putBoolean(PERSISTENCE_TAG, entity.isPersistent)
        put(ARMOR_ITEMS_TAG, KryptonEquipable.saveItems(entity.armorItems))
        put(HAND_ITEMS_TAG, KryptonEquipable.saveItems(entity.handItems))
        list(ARMOR_DROP_CHANCES_TAG) { entity.armorDropChances.forEach(::addFloat) }
        list(HAND_DROP_CHANCES_TAG) { entity.handDropChances.forEach(::addFloat) }
        putBoolean(LEFT_HANDED_TAG, entity.mainHand == MainHand.LEFT)
        if (!entity.hasAI) putBoolean(NO_AI_TAG, true)
    }

    @JvmStatic
    private fun loadChances(data: CompoundTag, name: String, chances: FloatArray) {
        if (!data.contains(name, ListTag.ID)) return
        val chancesData = data.getList(name, FloatTag.ID)
        for (i in 0 until chancesData.size()) {
            chances[i] = chancesData.getFloat(i)
        }
    }
}

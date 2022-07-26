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

object MobSerializer : EntitySerializer<KryptonMob> {

    override fun load(entity: KryptonMob, data: CompoundTag) {
        LivingEntitySerializer.load(entity, data)
        if (data.contains("CanPickUpLoot", ByteTag.ID)) entity.canPickUpLoot = data.getBoolean("CanPickUpLoot")
        entity.isPersistent = data.getBoolean("PersistenceRequired")

        KryptonEquipable.loadItems(data, "ArmorItems", entity.armorItems)
        KryptonEquipable.loadItems(data, "HandItems", entity.handItems)
        loadChances(data, "ArmorDropChances", entity.armorDropChances)
        loadChances(data, "HandDropChances", entity.handDropChances)

        entity.mainHand = if (data.getBoolean("LeftHanded")) MainHand.LEFT else MainHand.RIGHT
        entity.hasAI = !data.getBoolean("NoAI")
    }

    override fun save(entity: KryptonMob): CompoundTag.Builder = LivingEntitySerializer.save(entity).apply {
        boolean("CanPickUpLoot", entity.canPickUpLoot)
        boolean("PersistenceRequired", entity.isPersistent)
        put("ArmorItems", KryptonEquipable.saveItems(entity.armorItems))
        put("HandItems", KryptonEquipable.saveItems(entity.handItems))
        list("ArmorDropChances") { entity.armorDropChances.forEach(::addFloat) }
        list("HandDropChances") { entity.handDropChances.forEach(::addFloat) }
        boolean("LeftHanded", entity.mainHand == MainHand.LEFT)
        if (!entity.hasAI) boolean("NoAI", true)
    }

    @JvmStatic
    private fun loadChances(data: CompoundTag, name: String, chances: FloatArray) {
        if (!data.contains(name, ListTag.ID)) return
        val chancesData = data.getList(name, FloatTag.ID)
        for (i in 0 until chancesData.size) {
            chances[i] = chancesData.getFloat(i)
        }
    }
}

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
package org.kryptonmc.krypton.entity.serializer.aquatic

import org.kryptonmc.krypton.entity.aquatic.KryptonDolphin
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.MobSerializer
import org.kryptonmc.krypton.util.nbt.getBlockPos
import org.kryptonmc.krypton.util.nbt.putBlockPosParts
import org.kryptonmc.nbt.CompoundTag

object DolphinSerializer : EntitySerializer<KryptonDolphin> {

    private const val TREASURE_PREFIX = "TreasurePos"
    private const val GOT_FISH_TAG = "GotFish"
    private const val MOISTNESS_TAG = "Moistness"

    override fun load(entity: KryptonDolphin, data: CompoundTag) {
        MobSerializer.load(entity, data)
        entity.treasurePosition = data.getBlockPos(TREASURE_PREFIX)
        entity.hasGotFish = data.getBoolean(GOT_FISH_TAG)
        entity.skinMoisture = data.getInt(MOISTNESS_TAG)
    }

    override fun save(entity: KryptonDolphin): CompoundTag.Builder = MobSerializer.save(entity).apply {
        putBlockPosParts(entity.treasurePosition, TREASURE_PREFIX)
        putBoolean(GOT_FISH_TAG, entity.hasGotFish)
        putInt(MOISTNESS_TAG, entity.skinMoisture)
    }
}

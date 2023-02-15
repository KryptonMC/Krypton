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
package org.kryptonmc.krypton.entity.serializer.animal

import org.kryptonmc.api.entity.animal.type.PandaGene
import org.kryptonmc.krypton.entity.animal.KryptonPanda
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.putStringEnum
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag

object PandaSerializer : EntitySerializer<KryptonPanda> {

    private const val MAIN_GENE_TAG = "MainGene"
    private const val HIDDEN_GENE_TAG = "HiddenGene"
    private val GENE_NAMES = PandaGene.values().associateBy { it.name.lowercase() }

    override fun load(entity: KryptonPanda, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        if (data.contains(MAIN_GENE_TAG, StringTag.ID)) entity.knownGene = deserializeGene(data.getString(MAIN_GENE_TAG))
        if (data.contains(HIDDEN_GENE_TAG, StringTag.ID)) entity.hiddenGene = deserializeGene(data.getString(HIDDEN_GENE_TAG))
    }

    override fun save(entity: KryptonPanda): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        putStringEnum(MAIN_GENE_TAG, entity.knownGene)
        putStringEnum(HIDDEN_GENE_TAG, entity.hiddenGene)
    }

    @JvmStatic
    private fun deserializeGene(name: String): PandaGene = GENE_NAMES.getOrDefault(name, PandaGene.NORMAL)
}
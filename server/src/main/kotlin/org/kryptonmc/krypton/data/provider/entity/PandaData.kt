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
package org.kryptonmc.krypton.data.provider.entity

import org.kryptonmc.api.data.Keys
import org.kryptonmc.api.entity.animal.type.PandaGene
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.animal.KryptonPanda
import org.kryptonmc.krypton.entity.metadata.MetadataKeys

object PandaData {

    private val GENES = PandaGene.values()

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerMetadata<KryptonPanda, _, _>(Keys.KNOWN_GENE, MetadataKeys.PANDA.MAIN_GENE, ::gene) { it.ordinal.toByte() }
        registrar.registerMetadata<KryptonPanda, _, _>(Keys.KNOWN_GENE, MetadataKeys.PANDA.HIDDEN_GENE, ::gene) { it.ordinal.toByte() }
        registrar.registerFlag<KryptonPanda>(Keys.IS_SNEEZING, MetadataKeys.PANDA.FLAGS, 1)
        registrar.registerFlag<KryptonPanda>(Keys.IS_ROLLING, MetadataKeys.PANDA.FLAGS, 2)
        registrar.registerFlag<KryptonPanda>(Keys.IS_SITTING, MetadataKeys.PANDA.FLAGS, 3)
        registrar.registerFlag<KryptonPanda>(Keys.IS_LYING_ON_BACK, MetadataKeys.PANDA.FLAGS, 4)
        registrar.registerMutableNoSet(Keys.IS_SCARED, KryptonPanda::isScared)
        registrar.registerMetadata<KryptonPanda, _>(Keys.UNHAPPY_TIME, MetadataKeys.PANDA.UNHAPPY_TIMER)
        registrar.registerMetadata<KryptonPanda, _>(Keys.SNEEZING_TIME, MetadataKeys.PANDA.SNEEZE_TIMER)
        registrar.registerMetadata<KryptonPanda, _>(Keys.EATING_TIME, MetadataKeys.PANDA.EATING_TIMER)
    }

    @JvmStatic
    private fun gene(id: Byte): PandaGene = GENES.getOrNull(id.toInt()) ?: PandaGene.NORMAL
}

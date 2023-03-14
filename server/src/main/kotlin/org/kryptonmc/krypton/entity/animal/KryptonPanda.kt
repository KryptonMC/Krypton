/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.entity.animal

import org.kryptonmc.api.entity.animal.Panda
import org.kryptonmc.api.entity.animal.type.PandaGene
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.PandaSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonPanda(world: KryptonWorld) : KryptonAnimal(world), Panda {

    override val type: KryptonEntityType<KryptonPanda>
        get() = KryptonEntityTypes.PANDA
    override val serializer: EntitySerializer<KryptonPanda>
        get() = PandaSerializer

    override var knownGene: PandaGene
        get() = GENES.getOrNull(data.get(MetadataKeys.Panda.MAIN_GENE).toInt()) ?: PandaGene.NORMAL
        set(value) = data.set(MetadataKeys.Panda.MAIN_GENE, value.ordinal.toByte())
    override var hiddenGene: PandaGene
        get() = GENES.getOrNull(data.get(MetadataKeys.Panda.HIDDEN_GENE).toInt()) ?: PandaGene.NORMAL
        set(value) = data.set(MetadataKeys.Panda.HIDDEN_GENE, value.ordinal.toByte())
    override val isUnhappy: Boolean
        get() = unhappyTime > 0
    override var isEating: Boolean
        get() = eatingTime > 0
        set(value) {
            eatingTime = if (value) 1 else 0
        }
    override var isSneezing: Boolean
        get() = data.getFlag(MetadataKeys.Panda.FLAGS, FLAG_SNEEZING)
        set(value) {
            data.setFlag(MetadataKeys.Panda.FLAGS, FLAG_SNEEZING, value)
            if (!value) sneezingTime = 0
        }
    override var isRolling: Boolean
        get() = data.getFlag(MetadataKeys.Panda.FLAGS, FLAG_ROLLING)
        set(value) = data.setFlag(MetadataKeys.Panda.FLAGS, FLAG_ROLLING, value)
    override var isSitting: Boolean
        get() = data.getFlag(MetadataKeys.Panda.FLAGS, FLAG_SITTING)
        set(value) = data.setFlag(MetadataKeys.Panda.FLAGS, FLAG_SITTING, value)
    override var isLyingOnBack: Boolean
        get() = data.getFlag(MetadataKeys.Panda.FLAGS, FLAG_LYING_ON_BACK)
        set(value) = data.setFlag(MetadataKeys.Panda.FLAGS, FLAG_LYING_ON_BACK, value)
    override var unhappyTime: Int
        get() = data.get(MetadataKeys.Panda.UNHAPPY_TIMER)
        set(value) = data.set(MetadataKeys.Panda.UNHAPPY_TIMER, value)
    override var sneezingTime: Int
        get() = data.get(MetadataKeys.Panda.SNEEZE_TIMER)
        set(value) = data.set(MetadataKeys.Panda.SNEEZE_TIMER, value)
    override var eatingTime: Int
        get() = data.get(MetadataKeys.Panda.EATING_TIMER)
        set(value) = data.set(MetadataKeys.Panda.EATING_TIMER, value)
    override val isScared: Boolean
        get() = variant() == PandaGene.WORRIED && world.isThundering()

    init {
        if (!isBaby) canPickUpLoot = false
    }

    private fun variant(): PandaGene = variantFromGenes(knownGene, hiddenGene)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Panda.UNHAPPY_TIMER, 0)
        data.define(MetadataKeys.Panda.SNEEZE_TIMER, 0)
        data.define(MetadataKeys.Panda.EATING_TIMER, 0)
        data.define(MetadataKeys.Panda.MAIN_GENE, 0)
        data.define(MetadataKeys.Panda.HIDDEN_GENE, 0)
        data.define(MetadataKeys.Panda.FLAGS, 0)
    }

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.BAMBOO

    companion object {

        private const val FLAG_SNEEZING = 1
        private const val FLAG_ROLLING = 2
        private const val FLAG_SITTING = 3
        private const val FLAG_LYING_ON_BACK = 4
        private val GENES = PandaGene.values()

        private const val DEFAULT_MOVEMENT_SPEED = 0.15
        private const val DEFAULT_ATTACK_DAMAGE = 6.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)

        @JvmStatic
        private fun variantFromGenes(main: PandaGene, hidden: PandaGene): PandaGene {
            if (main.isRecessive) {
                if (main == hidden) return main
                return PandaGene.NORMAL
            }
            return main
        }
    }
}

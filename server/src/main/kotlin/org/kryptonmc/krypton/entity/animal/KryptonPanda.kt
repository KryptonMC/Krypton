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
package org.kryptonmc.krypton.entity.animal

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Panda
import org.kryptonmc.api.entity.animal.type.PandaGene
import org.kryptonmc.api.entity.animal.type.PandaGenes
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonPanda(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.PANDA, ATTRIBUTES), Panda {

    override var knownGene: PandaGene
        get() = Registries.PANDA_GENES[data[MetadataKeys.PANDA.MAIN_GENE].toInt()]!!
        set(value) = data.set(MetadataKeys.PANDA.MAIN_GENE, Registries.PANDA_GENES.idOf(value).toByte())
    override var hiddenGene: PandaGene
        get() = Registries.PANDA_GENES[data[MetadataKeys.PANDA.HIDDEN_GENE].toInt()]!!
        set(value) = data.set(MetadataKeys.PANDA.HIDDEN_GENE, Registries.PANDA_GENES.idOf(value).toByte())
    override val isUnhappy: Boolean
        get() = unhappyTime > 0
    override var isSneezing: Boolean
        get() = getFlag(2)
        set(value) {
            setFlag(2, value)
            if (!value) sneezingTime = 0
        }
    override var isEating: Boolean
        get() = eatingTime > 0
        set(value) {
            eatingTime = if (value) 1 else 0
        }
    override var isRolling: Boolean
        get() = getFlag(4)
        set(value) = setFlag(4, value)
    override var isSitting: Boolean
        get() = getFlag(8)
        set(value) = setFlag(8, value)
    override var isLyingOnBack: Boolean
        get() = getFlag(16)
        set(value) = setFlag(16, value)
    override var unhappyTime: Int
        get() = data[MetadataKeys.PANDA.UNHAPPY_TIMER]
        set(value) = data.set(MetadataKeys.PANDA.UNHAPPY_TIMER, value)
    override var sneezingTime: Int
        get() = data[MetadataKeys.PANDA.SNEEZE_TIMER]
        set(value) = data.set(MetadataKeys.PANDA.SNEEZE_TIMER, value)
    override var eatingTime: Int
        get() = data[MetadataKeys.PANDA.EATING_TIMER]
        set(value) = data.set(MetadataKeys.PANDA.EATING_TIMER, value)
    override val isScared: Boolean
        get() = variant === PandaGenes.WORRIED && world.isThundering

    private val variant: PandaGene
        get() = variantFromGenes(knownGene, hiddenGene)

    init {
        data.add(MetadataKeys.PANDA.UNHAPPY_TIMER)
        data.add(MetadataKeys.PANDA.SNEEZE_TIMER)
        data.add(MetadataKeys.PANDA.EATING_TIMER)
        data.add(MetadataKeys.PANDA.MAIN_GENE)
        data.add(MetadataKeys.PANDA.HIDDEN_GENE)
        data.add(MetadataKeys.PANDA.FLAGS)
        if (!isBaby) canPickUpLoot = false
    }

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.BAMBOO

    override fun load(tag: CompoundTag) {
        super.load(tag)
        if (tag.contains("MainGene")) knownGene = Registries.PANDA_GENES[Key.key(tag.getString("MainGene"))]!!
        if (tag.contains("HiddenGene")) hiddenGene = Registries.PANDA_GENES[Key.key(tag.getString("HiddenGene"))]!!
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        string("MainGene", knownGene.key().value())
        string("HiddenGene", hiddenGene.key().value())
    }

    private fun getFlag(index: Int): Boolean = data[MetadataKeys.PANDA.FLAGS].toInt() and index != 0

    private fun setFlag(index: Int, value: Boolean) {
        val old = data[MetadataKeys.PANDA.FLAGS].toInt()
        if (value) {
            data[MetadataKeys.PANDA.FLAGS] = (old or index).toByte()
        } else {
            data[MetadataKeys.PANDA.FLAGS] = (old and index.inv()).toByte()
        }
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MOVEMENT_SPEED, 0.15)
            .add(AttributeTypes.ATTACK_DAMAGE, 6.0)
            .build()

        @JvmStatic
        private fun variantFromGenes(main: PandaGene, hidden: PandaGene): PandaGene {
            if (main.isRecessive) {
                if (main === hidden) return main
                return PandaGenes.NORMAL
            }
            return main
        }
    }
}

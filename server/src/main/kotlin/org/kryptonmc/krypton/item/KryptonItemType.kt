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
package org.kryptonmc.krypton.item

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.item.ItemRarity
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.krypton.registry.Holder
import org.kryptonmc.krypton.registry.IntrusiveRegistryObject
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.world.block.KryptonBlock

@Suppress("LeakingThis") // The 'leak' doesn't need any of the data that wouldn't be initialized, it's just used as a key, so it's fine.
open class KryptonItemType(
    override val rarity: ItemRarity,
    override val maximumStackSize: Int,
    override val durability: Int,
    override val isEdible: Boolean,
    override val isFireResistant: Boolean,
    override val eatingSound: SoundEvent,
    override val drinkingSound: SoundEvent
) : ItemType, IntrusiveRegistryObject<KryptonItemType> {

    private var descriptionId: String? = null
    override val builtInRegistryHolder: Holder.Reference<KryptonItemType> = KryptonRegistries.ITEM.createIntrusiveHolder(this)

    override val canBreak: Boolean
        get() = durability > 0

    override fun key(): Key = KryptonRegistries.ITEM.getKey(this)

    override fun translationKey(): String = getOrCreateTranslationKey()

    protected fun getOrCreateTranslationKey(): String {
        if (descriptionId == null) descriptionId = Keys.translation("item", key())
        return descriptionId!!
    }

    override fun asBlock(): Block = KryptonRegistries.BLOCK.get(key())

    @Suppress("UNCHECKED_CAST")
    fun eq(tag: TagKey<ItemType>): Boolean = builtInRegistryHolder.eq(tag as TagKey<KryptonItemType>)

    companion object {

        @JvmField
        val BY_BLOCK: HashMap<KryptonBlock, KryptonItemType> = HashMap()

        @JvmStatic
        fun fromBlock(block: KryptonBlock): KryptonItemType = BY_BLOCK.getOrDefault(block, ItemTypes.AIR.downcast())
    }
}

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
package org.kryptonmc.krypton.item

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.item.ItemRarity
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.tags.TagKey
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
) : ItemType {

    private var descriptionId: String? = null
    private val builtInRegistryHolder = KryptonRegistries.ITEM.createIntrusiveHolder(this)

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
        fun fromBlock(block: KryptonBlock): KryptonItemType = BY_BLOCK.getOrDefault(block, ItemTypes.AIR.get().downcast())
    }
}

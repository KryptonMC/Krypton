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
package org.kryptonmc.krypton.item.meta

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.meta.FireworkRocketMeta
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.item.data.KryptonFireworkEffect
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag

class KryptonFireworkRocketMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: List<Component>,
    hideFlags: Int,
    canDestroy: Set<Block>,
    canPlaceOn: Set<Block>,
    override val effects: List<FireworkEffect>,
    override val flightDuration: Int
) : AbstractItemMeta<KryptonFireworkRocketMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn),
    FireworkRocketMeta {

    constructor(tag: CompoundTag) : this(
        tag.getInt("Damage"),
        tag.getBoolean("Unbreakable"),
        tag.getInt("CustomModelData"),
        tag.getDisplay<StringTag, Component>("Name", StringTag.ID, null) { GsonComponentSerializer.gson().deserialize(it.value) },
        tag.getDisplay<ListTag, List<Component>>("Lore", ListTag.ID, emptyList()) { list ->
            list.map { GsonComponentSerializer.gson().deserialize((it as StringTag).value) }
        }!!,
        tag.getInt("HideFlags"),
        tag.getList("CanDestroy", StringTag.ID).mapTo(mutableSetOf()) { Registries.BLOCK[Key.key((it as StringTag).value)]!! },
        tag.getList("CanPlaceOn", StringTag.ID).mapTo(mutableSetOf()) { Registries.BLOCK[Key.key((it as StringTag).value)]!! },
        tag.getCompound("Fireworks").getList("Explosions", CompoundTag.ID).map { KryptonFireworkEffect(it as CompoundTag) },
        tag.getCompound("Fireworks").getByte("Flight").toInt()
    )

    override fun copy(
        damage: Int,
        isUnbreakable: Boolean,
        customModelData: Int,
        name: Component?,
        lore: List<Component>,
        hideFlags: Int,
        canDestroy: Set<Block>,
        canPlaceOn: Set<Block>
    ): KryptonFireworkRocketMeta = copy(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn)

    override fun withEffects(effects: List<FireworkEffect>): KryptonFireworkRocketMeta = copy(effects = effects)

    override fun addEffect(effect: FireworkEffect): KryptonFireworkRocketMeta = withEffects(effects.plus(effect))

    override fun removeEffect(index: Int): KryptonFireworkRocketMeta = removeEffect(effects[index])

    override fun removeEffect(effect: FireworkEffect): KryptonFireworkRocketMeta = withEffects(effects.minus(effect))

    override fun withFlightDuration(duration: Int): KryptonFireworkRocketMeta = copy(flightDuration = duration)

    override fun toBuilder(): FireworkRocketMeta.Builder = Builder(this)

    private fun copy(
        damage: Int = this.damage,
        isUnbreakable: Boolean = this.isUnbreakable,
        customModelData: Int = this.customModelData,
        name: Component? = this.name,
        lore: List<Component> = this.lore,
        hideFlags: Int = this.hideFlags,
        canDestroy: Set<Block> = this.canDestroy,
        canPlaceOn: Set<Block> = this.canPlaceOn,
        effects: List<FireworkEffect> = this.effects,
        flightDuration: Int = this.flightDuration
    ): KryptonFireworkRocketMeta = KryptonFireworkRocketMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        effects,
        flightDuration
    )

    class Builder() : KryptonItemMetaBuilder<FireworkRocketMeta.Builder, FireworkRocketMeta>(), FireworkRocketMeta.Builder {

        private val effects = mutableListOf<FireworkEffect>()
        private var flightDuration = 0

        constructor(meta: FireworkRocketMeta) : this() {
            copyFrom(meta)
            effects.addAll(meta.effects)
            flightDuration = meta.flightDuration
        }

        override fun effects(effects: Iterable<FireworkEffect>): FireworkRocketMeta.Builder = apply {
            this.effects.clear()
            this.effects.addAll(effects)
        }

        override fun addEffect(effect: FireworkEffect): FireworkRocketMeta.Builder = apply { effects.add(effect) }

        override fun flightDuration(duration: Int): FireworkRocketMeta.Builder = apply { flightDuration = duration }

        override fun build(): KryptonFireworkRocketMeta = KryptonFireworkRocketMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore,
            hideFlags,
            canDestroy,
            canPlaceOn,
            effects,
            flightDuration
        )
    }
}

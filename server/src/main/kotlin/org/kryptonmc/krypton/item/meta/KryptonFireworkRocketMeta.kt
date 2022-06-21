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
package org.kryptonmc.krypton.item.meta

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.meta.FireworkRocketMeta
import org.kryptonmc.krypton.item.data.KryptonFireworkEffect
import org.kryptonmc.krypton.item.data.save
import org.kryptonmc.krypton.util.mapPersistentList
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.list

class KryptonFireworkRocketMeta(data: CompoundTag) : AbstractItemMeta<KryptonFireworkRocketMeta>(data), FireworkRocketMeta {

    override val effects: PersistentList<FireworkEffect> = data.getCompound("Fireworks")
        .getList("Explosions", CompoundTag.ID)
        .mapPersistentList { KryptonFireworkEffect.from(it as CompoundTag) }
    override val flightDuration: Int = data.getCompound("Fireworks").getByte("Flight").toInt()

    override fun copy(data: CompoundTag): KryptonFireworkRocketMeta = KryptonFireworkRocketMeta(data)

    override fun withEffects(effects: List<FireworkEffect>): KryptonFireworkRocketMeta = copy(data.putEffects(effects))

    override fun addEffect(effect: FireworkEffect): KryptonFireworkRocketMeta = withEffects(effects.add(effect))

    override fun removeEffect(index: Int): KryptonFireworkRocketMeta = withEffects(effects.removeAt(index))

    override fun removeEffect(effect: FireworkEffect): KryptonFireworkRocketMeta = withEffects(effects.remove(effect))

    override fun withFlightDuration(duration: Int): KryptonFireworkRocketMeta = copy(data.modifyFireworks { putByte("Flight", duration.toByte()) })

    override fun toBuilder(): FireworkRocketMeta.Builder = Builder(this)

    override fun toString(): String = "KryptonFireworkRocketMeta(${partialToString()}, effects=$effects, flightDuration=$flightDuration)"

    class Builder() : KryptonItemMetaBuilder<FireworkRocketMeta.Builder, FireworkRocketMeta>(), FireworkRocketMeta.Builder {

        private val effects = persistentListOf<FireworkEffect>().builder()
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

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            compound("Fireworks") {
                if (effects.isNotEmpty()) list("Explosions") { effects.forEach { add(it.save()) } }
                byte("Flight", flightDuration.toByte())
            }
        }

        override fun build(): KryptonFireworkRocketMeta = KryptonFireworkRocketMeta(buildData().build())
    }
}

private fun CompoundTag.modifyFireworks(modifier: CompoundTag.() -> CompoundTag): CompoundTag {
    if (!contains("Fireworks", CompoundTag.ID)) return this
    return modifier(getCompound("Fireworks"))
}

private fun CompoundTag.putEffects(effects: List<FireworkEffect>): CompoundTag {
    if (effects.isEmpty()) return modifyFireworks { remove("Explosions") }
    return modifyFireworks { put("Explosions", list { effects.forEach { add(it.save()) } }) }
}

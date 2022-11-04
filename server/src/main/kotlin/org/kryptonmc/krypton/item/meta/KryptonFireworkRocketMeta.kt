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

import com.google.common.collect.ImmutableList
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.meta.FireworkRocketMeta
import org.kryptonmc.krypton.item.data.KryptonFireworkEffect
import org.kryptonmc.krypton.item.data.save
import org.kryptonmc.krypton.util.BuilderCollection
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.compound
import org.kryptonmc.nbt.list

class KryptonFireworkRocketMeta(data: CompoundTag) : AbstractItemMeta<KryptonFireworkRocketMeta>(data), FireworkRocketMeta {

    override val effects: ImmutableList<FireworkEffect> =
        data.getCompound("Fireworks").mapToList("Explosions", CompoundTag.ID) { KryptonFireworkEffect.from(it as CompoundTag) }
    override val flightDuration: Int = data.getCompound("Fireworks").getByte("Flight").toInt()

    override fun copy(data: CompoundTag): KryptonFireworkRocketMeta = KryptonFireworkRocketMeta(data)

    override fun withEffects(effects: List<FireworkEffect>): KryptonFireworkRocketMeta = copy(data.putEffects(effects))

    override fun withEffect(effect: FireworkEffect): KryptonFireworkRocketMeta = copy(data.modifyEffects { it.add(effect.save()) })

    override fun withoutEffect(index: Int): KryptonFireworkRocketMeta = copy(data.modifyEffects { it.remove(index) })

    override fun withoutEffect(effect: FireworkEffect): KryptonFireworkRocketMeta = copy(data.modifyEffects { it.remove(effect.save()) })

    override fun withFlightDuration(duration: Int): KryptonFireworkRocketMeta =
        copy(data.update("Fireworks") { it.putByte("Flight", duration.toByte()) })

    override fun toBuilder(): FireworkRocketMeta.Builder = Builder(this)

    override fun toString(): String = "KryptonFireworkRocketMeta(${partialToString()}, effects=$effects, flightDuration=$flightDuration)"

    class Builder : KryptonItemMetaBuilder<FireworkRocketMeta.Builder, FireworkRocketMeta>, FireworkRocketMeta.Builder {

        private var effects: MutableCollection<FireworkEffect>
        private var flightDuration = 0

        constructor() : super() {
            effects = BuilderCollection()
        }

        constructor(meta: KryptonFireworkRocketMeta) : super(meta) {
            effects = BuilderCollection(meta.effects)
            flightDuration = meta.flightDuration
        }

        override fun effects(effects: Collection<FireworkEffect>): Builder = apply { this.effects = BuilderCollection(effects) }

        override fun flightDuration(duration: Int): Builder = apply { flightDuration = duration }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            compound("Fireworks") {
                if (effects.isNotEmpty()) list("Explosions") { effects.forEach { add(it.save()) } }
                putByte("Flight", flightDuration.toByte())
            }
        }

        override fun build(): KryptonFireworkRocketMeta = KryptonFireworkRocketMeta(buildData().build())
    }
}

private fun CompoundTag.modifyEffects(modifier: (ListTag) -> ListTag): CompoundTag {
    if (!contains("Fireworks", CompoundTag.ID)) return this
    return put("Fireworks", getCompound("Fireworks").update("Explosions", ListTag.ID, modifier))
}

private fun CompoundTag.putEffects(effects: List<FireworkEffect>): CompoundTag {
    if (effects.isEmpty()) return update("Fireworks") { remove("Explosions") }
    return update("Fireworks") { put("Explosions", list { effects.forEach { add(it.save()) } }) }
}

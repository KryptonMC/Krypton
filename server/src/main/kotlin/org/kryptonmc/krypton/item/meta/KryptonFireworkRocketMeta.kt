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
package org.kryptonmc.krypton.item.meta

import com.google.common.collect.ImmutableList
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.meta.FireworkRocketMeta
import org.kryptonmc.krypton.item.data.KryptonFireworkEffect
import org.kryptonmc.krypton.util.collection.BuilderCollection
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.compound
import org.kryptonmc.nbt.list
import java.util.function.UnaryOperator

class KryptonFireworkRocketMeta(data: CompoundTag) : AbstractItemMeta<KryptonFireworkRocketMeta>(data), FireworkRocketMeta {

    override val effects: ImmutableList<FireworkEffect> =
        mapToList(data.getCompound(FIREWORKS_TAG), EFFECTS_TAG, CompoundTag.ID) { KryptonFireworkEffect.load(it as CompoundTag) }
    override val flightDuration: Int = data.getCompound(FIREWORKS_TAG).getByte(FLIGHT_TAG).toInt()

    override fun copy(data: CompoundTag): KryptonFireworkRocketMeta = KryptonFireworkRocketMeta(data)

    override fun withEffects(effects: List<FireworkEffect>): KryptonFireworkRocketMeta =
        copy(data.update(FIREWORKS_TAG) { fireworks -> put(fireworks, EFFECTS_TAG, effects, KryptonFireworkEffect::save) })

    override fun withEffect(effect: FireworkEffect): KryptonFireworkRocketMeta =
        copy(modifyEffects(data) { it.add(KryptonFireworkEffect.save(effect)) })

    override fun withoutEffect(index: Int): KryptonFireworkRocketMeta = copy(modifyEffects(data) { it.remove(index) })

    override fun withoutEffect(effect: FireworkEffect): KryptonFireworkRocketMeta =
        copy(modifyEffects(data) { it.remove(KryptonFireworkEffect.save(effect)) })

    override fun withFlightDuration(duration: Int): KryptonFireworkRocketMeta =
        copy(data.update(FIREWORKS_TAG) { it.putByte(FLIGHT_TAG, duration.toByte()) })

    override fun toBuilder(): FireworkRocketMeta.Builder = Builder(this)

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
            compound(FIREWORKS_TAG) {
                if (effects.isNotEmpty()) list(EFFECTS_TAG) { effects.forEach { add(KryptonFireworkEffect.save(it)) } }
                putByte(FLIGHT_TAG, flightDuration.toByte())
            }
        }

        override fun build(): KryptonFireworkRocketMeta = KryptonFireworkRocketMeta(buildData().build())
    }

    companion object {

        private const val FIREWORKS_TAG = "Fireworks"
        private const val EFFECTS_TAG = "Explosions"
        private const val FLIGHT_TAG = "Flight"

        @JvmStatic
        private fun modifyEffects(data: CompoundTag, modifier: UnaryOperator<ListTag>): CompoundTag {
            if (!data.contains(FIREWORKS_TAG, CompoundTag.ID)) return data
            return data.put(FIREWORKS_TAG, data.getCompound(FIREWORKS_TAG).update(EFFECTS_TAG, ListTag.ID, modifier))
        }
    }
}

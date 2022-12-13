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
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.meta.CrossbowMeta
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.util.BuilderCollection
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.list

class KryptonCrossbowMeta(data: CompoundTag) : AbstractItemMeta<KryptonCrossbowMeta>(data), CrossbowMeta {

    override val isCharged: Boolean = data.getBoolean(CHARGED_TAG)
    override val projectiles: ImmutableList<ItemStack> =
        mapToList(data, PROJECTILES_TAG, CompoundTag.ID) { KryptonItemStack.from(it as CompoundTag) }

    override fun copy(data: CompoundTag): KryptonCrossbowMeta = KryptonCrossbowMeta(data)

    override fun withCharged(charged: Boolean): KryptonCrossbowMeta = copy(data.putBoolean(CHARGED_TAG, charged))

    override fun withProjectiles(projectiles: List<ItemStack>): KryptonCrossbowMeta =
        copy(put(data, PROJECTILES_TAG, projectiles) { it.downcast().save() })

    override fun withProjectile(projectile: ItemStack): KryptonCrossbowMeta =
        copy(data.update(PROJECTILES_TAG, CompoundTag.ID) { it.add(projectile.downcast().save()) })

    override fun withoutProjectile(index: Int): KryptonCrossbowMeta = copy(data.update(PROJECTILES_TAG, CompoundTag.ID) { it.remove(index) })

    override fun withoutProjectile(projectile: ItemStack): KryptonCrossbowMeta =
        copy(data.update(PROJECTILES_TAG, CompoundTag.ID) { it.remove(projectile.downcast().save()) })

    override fun toBuilder(): CrossbowMeta.Builder = Builder(this)

    override fun toString(): String = "KryptonCrossbowMeta(${partialToString()}, isCharged=$isCharged, projectiles=$projectiles)"

    class Builder : KryptonItemMetaBuilder<CrossbowMeta.Builder, CrossbowMeta>, CrossbowMeta.Builder {

        private var charged = false
        private var projectiles: MutableCollection<ItemStack>

        constructor() : super() {
            projectiles = BuilderCollection()
        }

        constructor(meta: KryptonCrossbowMeta) : super(meta) {
            charged = meta.isCharged
            projectiles = BuilderCollection(meta.projectiles)
        }

        override fun charged(value: Boolean): Builder = apply { charged = value }

        override fun projectiles(projectiles: Collection<ItemStack>): Builder = apply { this.projectiles = BuilderCollection(projectiles) }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            putBoolean(CHARGED_TAG, charged)
            if (projectiles.isNotEmpty()) list(PROJECTILES_TAG) { projectiles.forEach { add(it.downcast().save()) } }
        }

        override fun build(): KryptonCrossbowMeta = KryptonCrossbowMeta(buildData().build())
    }

    companion object {

        private const val CHARGED_TAG = "Charged"
        private const val PROJECTILES_TAG = "ChargedProjectiles"
    }
}

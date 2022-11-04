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

    override val isCharged: Boolean = data.getBoolean("Charged")
    override val projectiles: ImmutableList<ItemStack> =
        data.mapToList("ChargedProjectiles", CompoundTag.ID) { KryptonItemStack.from(it as CompoundTag) }

    override fun copy(data: CompoundTag): KryptonCrossbowMeta = KryptonCrossbowMeta(data)

    override fun withCharged(charged: Boolean): KryptonCrossbowMeta = copy(data.putBoolean("Charged", charged))

    override fun withProjectiles(projectiles: List<ItemStack>): KryptonCrossbowMeta = copy(data.putProjectiles(projectiles))

    override fun withProjectile(projectile: ItemStack): KryptonCrossbowMeta =
        copy(data.update("ChargedProjectiles", CompoundTag.ID) { it.add(projectile.downcast().save()) })

    override fun withoutProjectile(index: Int): KryptonCrossbowMeta = copy(data.update("ChargedProjectiles", CompoundTag.ID) { it.remove(index) })

    override fun withoutProjectile(projectile: ItemStack): KryptonCrossbowMeta =
        copy(data.update("ChargedProjectiles", CompoundTag.ID) { it.remove(projectile.downcast().save()) })

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
            putBoolean("Charged", charged)
            if (projectiles.isNotEmpty()) list("ChargedProjectiles") { projectiles.forEach { add(it.downcast().save()) } }
        }

        override fun build(): KryptonCrossbowMeta = KryptonCrossbowMeta(buildData().build())
    }
}

private fun CompoundTag.putProjectiles(projectiles: List<ItemStack>): CompoundTag {
    if (projectiles.isEmpty()) return remove("ChargedProjectiles")
    return put("ChargedProjectiles", list { projectiles.forEach { add(it.downcast().save()) } })
}

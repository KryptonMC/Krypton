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
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.meta.CrossbowMeta
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.mapPersistentList
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.list

@Suppress("EqualsOrHashCode")
class KryptonCrossbowMeta(data: CompoundTag) : AbstractItemMeta<KryptonCrossbowMeta>(data), CrossbowMeta {

    override val isCharged: Boolean = data.getBoolean("Charged")
    override val projectiles: PersistentList<ItemStack> = data.getList("ChargedProjectiles", CompoundTag.ID)
        .mapPersistentList { KryptonItemStack.from(it as CompoundTag) }

    override fun copy(data: CompoundTag): KryptonCrossbowMeta = KryptonCrossbowMeta(data)

    override fun withCharged(charged: Boolean): KryptonCrossbowMeta = copy(data.putBoolean("Charged", charged))

    override fun withProjectiles(projectiles: List<ItemStack>): KryptonCrossbowMeta = KryptonCrossbowMeta(data.putProjectiles(projectiles))

    override fun addProjectile(projectile: ItemStack): KryptonCrossbowMeta = withProjectiles(projectiles.add(projectile))

    override fun removeProjectile(index: Int): KryptonCrossbowMeta = withProjectiles(projectiles.removeAt(index))

    override fun removeProjectile(projectile: ItemStack): KryptonCrossbowMeta = withProjectiles(projectiles.remove(projectile))

    override fun toBuilder(): CrossbowMeta.Builder = Builder(this)

    override fun toString(): String = "KryptonCrossbowMeta(${partialToString()}, isCharged=$isCharged, projectiles=$projectiles)"

    class Builder() : KryptonItemMetaBuilder<CrossbowMeta.Builder, CrossbowMeta>(), CrossbowMeta.Builder {

        private var charged = false
        private val projectiles = persistentListOf<ItemStack>().builder()

        constructor(meta: CrossbowMeta) : this() {
            copyFrom(meta)
            charged = meta.isCharged
            projectiles.addAll(meta.projectiles)
        }

        override fun charged(value: Boolean): CrossbowMeta.Builder = apply { charged = value }

        override fun projectiles(projectiles: Iterable<ItemStack>): CrossbowMeta.Builder = apply {
            this.projectiles.clear()
            this.projectiles.addAll(projectiles)
        }

        override fun addProjectile(projectile: ItemStack): CrossbowMeta.Builder = apply { projectiles.add(projectile) }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            boolean("Charged", charged)
            if (projectiles.isNotEmpty()) list("ChargedProjectiles") { projectiles.forEach { add((it as KryptonItemStack).save()) } }
        }

        override fun build(): KryptonCrossbowMeta = KryptonCrossbowMeta(buildData().build())
    }
}

private fun CompoundTag.putProjectiles(projectiles: List<ItemStack>): CompoundTag {
    if (projectiles.isNotEmpty()) return remove("ChargedProjectiles")
    return put("ChargedProjectiles", list { projectiles.forEach { add((it as KryptonItemStack).save()) } })
}

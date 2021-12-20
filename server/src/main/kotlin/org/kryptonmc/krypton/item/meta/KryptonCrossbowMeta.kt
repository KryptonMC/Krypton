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
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.meta.CrossbowMeta
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag

class KryptonCrossbowMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: List<Component>,
    hideFlags: Int,
    canDestroy: Set<Block>,
    canPlaceOn: Set<Block>,
    override val isCharged: Boolean,
    override val projectiles: List<ItemStack>
) : AbstractItemMeta<KryptonCrossbowMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn), CrossbowMeta {

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
        tag.getBoolean("Charged"),
        tag.getList("ChargedProjectiles", CompoundTag.ID).map { KryptonItemStack(it as CompoundTag) }
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
    ): KryptonCrossbowMeta = copy(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn)

    override fun withCharged(charged: Boolean): KryptonCrossbowMeta = copy(charged = charged)

    override fun withProjectiles(projectiles: List<ItemStack>): KryptonCrossbowMeta = copy(projectiles = projectiles)

    override fun addProjectile(projectile: ItemStack): KryptonCrossbowMeta = withProjectiles(projectiles.plus(projectile))

    override fun removeProjectile(index: Int): KryptonCrossbowMeta = removeProjectile(projectiles[index])

    override fun removeProjectile(projectile: ItemStack): KryptonCrossbowMeta = withProjectiles(projectiles.minus(projectile))

    override fun toBuilder(): CrossbowMeta.Builder = Builder(this)

    private fun copy(
        damage: Int = this.damage,
        isUnbreakable: Boolean = this.isUnbreakable,
        customModelData: Int = this.customModelData,
        name: Component? = this.name,
        lore: List<Component> = this.lore,
        hideFlags: Int = this.hideFlags,
        canDestroy: Set<Block> = this.canDestroy,
        canPlaceOn: Set<Block> = this.canPlaceOn,
        charged: Boolean = isCharged,
        projectiles: List<ItemStack> = this.projectiles
    ): KryptonCrossbowMeta = KryptonCrossbowMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        charged,
        projectiles
    )

    class Builder() : KryptonItemMetaBuilder<CrossbowMeta.Builder, CrossbowMeta>(), CrossbowMeta.Builder {

        private var charged = false
        private val projectiles = mutableListOf<ItemStack>()

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

        override fun build(): KryptonCrossbowMeta = KryptonCrossbowMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore,
            hideFlags,
            canDestroy,
            canPlaceOn,
            charged,
            projectiles
        )
    }
}

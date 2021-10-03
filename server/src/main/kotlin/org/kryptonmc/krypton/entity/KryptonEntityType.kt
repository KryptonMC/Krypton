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
package org.kryptonmc.krypton.entity

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType

@JvmRecord
data class KryptonEntityType<T : Entity>(
    private val key: Key,
    override val isSummonable: Boolean,
    override val translation: TranslatableComponent
) : EntityType<T> {

    override fun key(): Key = key

    object Factory : EntityType.Factory {

        override fun <T : Entity> of(
            key: Key,
            summonable: Boolean,
            translation: TranslatableComponent
        ): EntityType<T> = KryptonEntityType(key, summonable, translation)

        override fun <T : Entity> of(key: Key, summonable: Boolean): EntityType<T> = KryptonEntityType(
            key,
            summonable,
            Component.translatable("entity.${key.namespace()}.${key.value().replace('/', '.')}")
        )
    }
}

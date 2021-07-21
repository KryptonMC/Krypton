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
package org.kryptonmc.krypton.tags

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.Sets
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey

object StaticTags {

    private val HELPER_IDS = mutableSetOf<ResourceKey<*>>()
    private val HELPERS = mutableListOf<StaticTagHelper<*>>()
    private val KNOWN_HELPERS = setOf(BlockTags.HELPER, ItemTags.HELPER, FluidTags.HELPER, EntityTypeTags.HELPER, GameEventTags.HELPER)

    fun bootstrap() = checkKnownHelpersLoaded()

    fun <T : Any> create(key: ResourceKey<out Registry<T>>, directory: String): StaticTagHelper<T> {
        require(HELPER_IDS.add(key)) { "Duplicate entry for static tag collection $key!" }
        return StaticTagHelper(key, directory).apply { HELPERS.add(this) }
    }

    fun resetAll(container: TagContainer) = HELPERS.forEach { it.reset(container) }

    fun missing(container: TagContainer): Multimap<ResourceKey<out Registry<*>>, Key> = HashMultimap.create<ResourceKey<out Registry<*>>, Key>().apply {
        HELPERS.forEach { putAll(it.key, it.missing(container)) }
    }

    fun visitHelpers(consumer: (StaticTagHelper<*>) -> Unit) = HELPERS.forEach(consumer)

    fun createContainer(): TagContainer {
        val builder = TagContainer.Builder()
        checkKnownHelpersLoaded()
        HELPERS.forEach { it.addToCollection(builder) }
        return builder.build()
    }

    private fun checkKnownHelpersLoaded() {
        val keys = KNOWN_HELPERS.mapTo(mutableSetOf()) { it.key }
        check(Sets.difference(HELPER_IDS, keys).isEmpty()) { "Missing required helper registrations!" }
    }
}

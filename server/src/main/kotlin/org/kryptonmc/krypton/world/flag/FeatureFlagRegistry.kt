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
package org.kryptonmc.krypton.world.flag

import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.ImmutableMaps
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult
import java.util.Collections
import java.util.IdentityHashMap
import java.util.function.Consumer

class FeatureFlagRegistry(private val universe: FeatureFlagUniverse, private val allFlags: FeatureFlagSet,
                          private val names: Map<Key, FeatureFlag>) {

    fun isSubset(flags: FeatureFlagSet): Boolean = flags.isSubsetOf(allFlags)

    fun allFlags(): FeatureFlagSet = allFlags

    fun fromNames(names: Iterable<Key>): FeatureFlagSet = fromNames(names) { LOGGER.warn("Unknown feature flag $it.") }

    fun subset(flags: Array<out FeatureFlag>): FeatureFlagSet = FeatureFlagSet.create(universe, flags.asList())

    fun fromNames(names: Iterable<Key>, missing: Consumer<Key>): FeatureFlagSet {
        val flags = Collections.newSetFromMap<FeatureFlag>(IdentityHashMap())
        names.forEach { name ->
            val flag = this.names.get(name)
            if (flag == null) missing.accept(name) else flags.add(flag)
        }
        return FeatureFlagSet.create(universe, flags)
    }

    fun toNames(flags: FeatureFlagSet): Set<Key> {
        val result = HashSet<Key>()
        names.forEach { (name, flag) -> if (flags.contains(flag)) result.add(name) }
        return result
    }

    fun codec(): Codec<FeatureFlagSet> {
        return Codecs.KEY.listOf().comapFlatMap({ names ->
            val missing = HashSet<Key>()
            val set = fromNames(names) { missing.add(it) }
            if (missing.isNotEmpty()) DataResult.error("Unknown feature IDs: $missing", set) else DataResult.success(set)
        }, { ImmutableLists.copyOf(toNames(it)) })
    }

    class Builder(universeId: String) {

        private val universe = FeatureFlagUniverse(universeId)
        private var id = 0
        private val flags = LinkedHashMap<Key, FeatureFlag>()

        fun createVanilla(name: String): FeatureFlag = create(Key.key(Key.MINECRAFT_NAMESPACE, name))

        fun create(name: Key): FeatureFlag {
            if (id >= MAX_CONTAINER_SIZE) error("Too many feature flags! Max: $MAX_CONTAINER_SIZE.")
            val flag = FeatureFlag(universe, id++)
            require(flags.put(name, flag) == null) { "Duplicate feature flag $name!" }
            return flag
        }

        fun build(): FeatureFlagRegistry = FeatureFlagRegistry(universe, FeatureFlagSet.create(universe, flags.values), ImmutableMaps.copyOf(flags))
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private const val MAX_CONTAINER_SIZE = 64
    }
}

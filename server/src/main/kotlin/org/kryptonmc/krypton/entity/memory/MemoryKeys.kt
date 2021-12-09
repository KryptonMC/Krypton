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
package org.kryptonmc.krypton.entity.memory

import net.kyori.adventure.key.Key
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.GlobalPosition
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.Codec
import org.kryptonmc.nbt.Tag

@Catalogue(MemoryKey::class)
object MemoryKeys {

    @JvmField val ADMIRING_DISABLED = register<Boolean>("admiring_disabled", Codecs.BOOLEAN)
    @JvmField val ADMIRING_ITEM = register<Boolean>("admiring_item", Codecs.BOOLEAN)
    @JvmField val ANGRY_AT = register("angry_at", Codecs.UUID)
    @JvmField val GOLEM_DETECTED_RECENTLY = register<Boolean>("golem_detected_recently", Codecs.BOOLEAN)
    @JvmField val HAS_HUNTING_COOLDOWN = register<Boolean>("has_hunting_cooldown", Codecs.BOOLEAN)
    @JvmField val HOME = register("home", GlobalPosition.CODEC)
    @JvmField val HUNTED_RECENTLY = register<Boolean>("hunted_recently", Codecs.BOOLEAN)
    @JvmField val IS_TEMPTED = register<Boolean>("is_tempted", Codecs.BOOLEAN)
    @JvmField val JOB_SITE = register("job_site", GlobalPosition.CODEC)
    @JvmField val LAST_SLEPT = register<Long>("last_slept", Codecs.LONG)
    @JvmField val LAST_WOKEN = register<Long>("last_woken", Codecs.LONG)
    @JvmField val LAST_WORKED_AT_POI = register<Long>("last_worked_at_poi", Codecs.LONG)
    @JvmField val LONG_JUMP_COOLING_DOWN = register<Int>("long_jump_cooling_down", Codecs.INTEGER)
    @JvmField val MEETING_POINT = register("meeting_point", GlobalPosition.CODEC)
    @JvmField val PLAY_DEAD_TICKS = register<Int>("play_dead_ticks", Codecs.INTEGER)
    @JvmField val POTENTIAL_JOB_SITE = register("potential_job_site", GlobalPosition.CODEC)
    @JvmField val RAM_COOLDOWN_TICKS = register<Int>("ram_cooldown_ticks", Codecs.INTEGER)
    @JvmField val TEMPTATION_COOLDOWN_TICKS = register<Int>("temptation_cooldown_ticks", Codecs.INTEGER)
    @JvmField val UNIVERSAL_ANGER = register<Boolean>("universal_anger", Codecs.BOOLEAN)

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    private fun <T : Any> register(name: String, codec: Codec<out Tag, T>): MemoryKey<T> {
        val key = Key.key(name)
        return InternalRegistries.MEMORIES.register(
            key,
            MemoryKey(key, codec as Codec<Tag, T>) as MemoryKey<Any>
        ) as MemoryKey<T>
    }
}

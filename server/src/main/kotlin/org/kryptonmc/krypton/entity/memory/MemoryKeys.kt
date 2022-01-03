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
package org.kryptonmc.krypton.entity.memory

import net.kyori.adventure.key.Key
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.GlobalPosition
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.Codec
import org.kryptonmc.nbt.Tag
import java.util.UUID

@Catalogue(MemoryKey::class)
object MemoryKeys {

    @JvmField val ADMIRING_DISABLED: MemoryKey<Boolean> = register("admiring_disabled", Codecs.BOOLEAN)
    @JvmField val ADMIRING_ITEM: MemoryKey<Boolean> = register("admiring_item", Codecs.BOOLEAN)
    @JvmField val ANGRY_AT: MemoryKey<UUID> = register("angry_at", Codecs.UUID)
    @JvmField val GOLEM_DETECTED_RECENTLY: MemoryKey<Boolean> = register("golem_detected_recently", Codecs.BOOLEAN)
    @JvmField val HAS_HUNTING_COOLDOWN: MemoryKey<Boolean> = register("has_hunting_cooldown", Codecs.BOOLEAN)
    @JvmField val HOME: MemoryKey<GlobalPosition> = register("home", GlobalPosition.CODEC)
    @JvmField val HUNTED_RECENTLY: MemoryKey<Boolean> = register("hunted_recently", Codecs.BOOLEAN)
    @JvmField val IS_TEMPTED: MemoryKey<Boolean> = register("is_tempted", Codecs.BOOLEAN)
    @JvmField val JOB_SITE: MemoryKey<GlobalPosition> = register("job_site", GlobalPosition.CODEC)
    @JvmField val LAST_SLEPT: MemoryKey<Long> = register("last_slept", Codecs.LONG)
    @JvmField val LAST_WOKEN: MemoryKey<Long> = register("last_woken", Codecs.LONG)
    @JvmField val LAST_WORKED_AT_POI: MemoryKey<Long> = register("last_worked_at_poi", Codecs.LONG)
    @JvmField val LONG_JUMP_COOLING_DOWN: MemoryKey<Int> = register("long_jump_cooling_down", Codecs.INTEGER)
    @JvmField val MEETING_POINT: MemoryKey<GlobalPosition> = register("meeting_point", GlobalPosition.CODEC)
    @JvmField val PLAY_DEAD_TICKS: MemoryKey<Int> = register("play_dead_ticks", Codecs.INTEGER)
    @JvmField val POTENTIAL_JOB_SITE: MemoryKey<GlobalPosition> = register("potential_job_site", GlobalPosition.CODEC)
    @JvmField val RAM_COOLDOWN_TICKS: MemoryKey<Int> = register("ram_cooldown_ticks", Codecs.INTEGER)
    @JvmField val TEMPTATION_COOLDOWN_TICKS: MemoryKey<Int> = register("temptation_cooldown_ticks", Codecs.INTEGER)
    @JvmField val UNIVERSAL_ANGER: MemoryKey<Boolean> = register("universal_anger", Codecs.BOOLEAN)

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

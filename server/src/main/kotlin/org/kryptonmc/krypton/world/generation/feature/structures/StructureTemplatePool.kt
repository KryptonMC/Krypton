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
package org.kryptonmc.krypton.world.generation.feature.structures

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.kyori.adventure.key.Key
import org.kryptonmc.api.util.StringSerializable
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.RegistryFileCodec
import org.kryptonmc.krypton.util.KEY_CODEC
import org.kryptonmc.krypton.util.codec
import org.kryptonmc.krypton.world.generation.structure.template.StructureProcessor

class StructureTemplatePool {

    private val name: Key
    private val fallback: Key
    private val rawTemplates: MutableList<Pair<StructurePoolElement, Int>>
    private val templates: MutableList<StructurePoolElement>
    private var maxSize = Int.MIN_VALUE

    constructor(name: Key, fallback: Key, rawTemplates: MutableList<Pair<StructurePoolElement, Int>>) {
        this.name = name
        this.fallback = fallback
        this.rawTemplates = rawTemplates
        this.templates = mutableListOf()
        rawTemplates.forEach {
            val element = it.first
            for (i in 0 until it.second) templates.add(element)
        }
    }

    constructor(name: Key, fallback: Key, rawTemplates: List<Pair<(Projection) -> StructurePoolElement, Int>>, projection: Projection) {
        this.name = name
        this.fallback = fallback
        this.rawTemplates = mutableListOf()
        this.templates = mutableListOf()
        rawTemplates.forEach {
            val element = it.first.invoke(projection)
            this.rawTemplates.add(Pair(element, it.second))
            for (i in 0 until it.second) templates.add(element)
        }
    }

    enum class Projection(override val serialized: String, val processors: List<StructureProcessor>) : StringSerializable {

        TERRAIN_MATCHING("terrain_matching", emptyList()), // FIXME
        RIGID("rigid", emptyList());

        companion object {

            private val BY_NAME = values().associateBy { it.serialized }
            val CODEC: Codec<Projection> = values().codec { BY_NAME[it] }
        }
    }

    companion object {

        val DIRECT_CODEC: Codec<StructureTemplatePool> = RecordCodecBuilder.create {
            it.group(
                KEY_CODEC.fieldOf("name").forGetter(StructureTemplatePool::name),
                KEY_CODEC.fieldOf("fallback").forGetter(StructureTemplatePool::fallback),
                Codec.mapPair(StructurePoolElement.CODEC.fieldOf("element"), Codec.intRange(1, 150).fieldOf("weight")).codec().listOf().fieldOf("elements").forGetter(StructureTemplatePool::rawTemplates)
            ).apply(it) { name, fallback, templates -> StructureTemplatePool(name, fallback, templates) }
        }
        val CODEC: Codec<() -> StructureTemplatePool> = RegistryFileCodec(InternalResourceKeys.TEMPLATE_POOL, DIRECT_CODEC)
    }
}

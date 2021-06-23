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
package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import java.util.function.Supplier

class V1510(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        put("minecraft:command_block_minecart", remove("minecraft:commandblock_minecart"))
        put("minecraft:end_crystal", remove("minecraft:ender_crystal"))
        put("minecraft:snow_golem", remove("minecraft:snowman"))
        put("minecraft:evoker", remove("minecraft:evocation_illager"))
        put("minecraft:evoker_fangs", remove("minecraft:evocation_fangs"))
        put("minecraft:illusioner", remove("minecraft:illusion_illager"))
        put("minecraft:vindicator", remove("minecraft:vindication_illager"))
        put("minecraft:iron_golem", remove("minecraft:villager_golem"))
        put("minecraft:experience_orb", remove("minecraft:xp_orb"))
        put("minecraft:experience_bottle", remove("minecraft:xp_bottle"))
        put("minecraft:eye_of_ender", remove("minecraft:eye_of_ender_signal"))
        put("minecraft:firework_rocket", remove("minecraft:fireworks_rocket"))
    }
}

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
package org.kryptonmc.krypton.util.datafix.fixes.entity

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.References

class ShulkerRotationFix(outputSchema: Schema) : NamedEntityFix(outputSchema, false, "ShulkerRotationFix", References.ENTITY, "minecraft:shulker") {

    override fun fix(typed: Typed<*>): Typed<*> = typed.update(remainderFinder()) { shulker ->
        val rotation = shulker["Rotation"].asList { it.asDouble(180.0) }
        if (rotation.isEmpty()) return@update shulker
        rotation[0] = rotation[0] - 180.0
        shulker.set("Rotation", shulker.createList(rotation.stream().map(shulker::createDouble)))
    }
}

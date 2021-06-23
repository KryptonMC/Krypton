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
package org.kryptonmc.krypton.util.datafix.fixes.uuid

import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class BlockEntityUUIDFix(outputSchema: Schema) : UUIDFix(outputSchema, References.BLOCK_ENTITY) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped("BlockEntityUUIDFix", inputSchema.getType(typeReference)) { typed ->
        val temp = typed.updateNamedChoice("minecraft:conduit") { it.updateConduit() }
        temp.updateNamedChoice("minecraft:skull") { it.updateSkull() }
    }

    private fun Dynamic<*>.updateSkull() = get("Owner").get()
        .map { replaceUUIDString("Id", "Id").orElse(it) }
        .map { remove("Owner").set("SkullOwner", it) }
        .result()
        .orElse(this)

    private fun Dynamic<*>.updateConduit() = replaceUUIDMLTag("target_uuid", "Target").orElse(this)
}

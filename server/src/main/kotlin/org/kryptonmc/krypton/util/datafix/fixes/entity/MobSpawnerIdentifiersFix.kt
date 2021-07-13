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
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.api.util.getIfPresent
import org.kryptonmc.krypton.util.datafix.References

class MobSpawnerIdentifiersFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val spawnerType = outputSchema.getType(References.UNTAGGED_SPAWNER)
        return fixTypeEverywhereTyped("MobSpawnerIdentifiersFix", inputSchema.getType(References.UNTAGGED_SPAWNER), spawnerType) {
            var data = it[remainderFinder()]
            data = data.set("id", data.createString("MobSpawner"))
            spawnerType.readTyped(data.fix()).result().getIfPresent()?.first ?: it
        }
    }

    private fun Dynamic<*>.fix(): Dynamic<*> {
        if (get("id").asString("") != "MobSpawner") return this
        var temp = this
        val entityId = get("EntityId").asString().result()
        if (entityId.isPresent) {
            var spawnData = DataFixUtils.orElse(get("SpawnData").result(), emptyMap())
            spawnData = spawnData.set("id", spawnData.createString(entityId.get().ifEmpty { "Pig" }))
            temp = temp.set("SpawnData", spawnData)
            temp = temp.remove("EntityId")
        }
        val potentialSpawns = temp["SpawnPotentials"].asStreamOpt().result()
        if (potentialSpawns.isPresent) temp = temp.set("SpawnPotentials", temp.createList(potentialSpawns.get().map {
            val type = it["Type"].asString().result()
            if (type.isPresent) {
                val properties = DataFixUtils.orElse(it["Properties"].result(), it.emptyMap()).set("id", it.createString(type.get()))
                it.set("Entity", properties).remove("Type").remove("Properties")
            } else {
                it
            }
        }))
        return temp
    }
}

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

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class WorldUUIDFix(outputSchema: Schema) : UUIDFix(outputSchema, References.LEVEL) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped("LevelUUIDFix", inputSchema.getType(typeReference)) { typed ->
        typed.updateTyped(remainderFinder()) { typed1 ->
            typed1.update(remainderFinder()) {
                var temp = it.updateCustomBossEvents()
                temp = temp.updateDragonFight()
                temp.updateWanderingTrader()
            }
        }
    }

    private fun Dynamic<*>.updateWanderingTrader() = replaceUUIDString("WanderingTraderId", "WanderingTraderId").orElse(this)

    private fun Dynamic<*>.updateDragonFight() = update("DimensionData") { data ->
        data.updateMapValues { values ->
            values.mapSecond { second ->
                second.update("DragonFight") { it.replaceUUIDLeastMost("DragonUUID", "Dragon").orElse(it) }
            }
        }
    }

    private fun Dynamic<*>.updateCustomBossEvents() = update("CustomBossEvents") { events ->
        events.updateMapValues { values ->
            values.mapSecond { second ->
                second.update("Players") { players -> events.createList(players.asStream().map { it.createUUIDFromML().orElse(it) }) }
            }
        }
    }
}

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

import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Dynamic

class SplitElderGuardianFix(outputSchema: Schema, changesType: Boolean) : SimpleSplitEntityFix("SplitElderGuardianFix", outputSchema, changesType) {

    override fun newNameAndTag(entityName: String, data: Dynamic<*>): Pair<String, Dynamic<*>> =
        Pair.of(if (entityName == "Guardian" && data["Elder"].asBoolean(false)) "ElderGuardian" else entityName, data)
}

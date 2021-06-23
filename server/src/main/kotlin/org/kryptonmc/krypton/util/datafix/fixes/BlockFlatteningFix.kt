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
package org.kryptonmc.krypton.util.datafix.fixes

import com.mojang.datafixers.DSL.intType
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DSL.or
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.fixes.BlockStateData.upgradeBlock
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema
import org.kryptonmc.krypton.util.datafix.schema.ensureNamespaced
import java.util.function.Function

class BlockFlatteningFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val oldType = inputSchema.getType(References.BLOCK_NAME)
        val newType = outputSchema.getType(References.BLOCK_NAME)
        val oldNameType = named(References.BLOCK_NAME.typeName(), or(intType(), NamespacedSchema.NAMESPACED_STRING))
        val newNameType = named(References.BLOCK_NAME.typeName(), NamespacedSchema.NAMESPACED_STRING)
        check(oldType == oldNameType && newType == newNameType) { "Expected types and actual types don't match! Expected $oldNameType, got $oldType and expected $newNameType, got $newType!" }
        return fixTypeEverywhere("BlockFlatteningFix", oldNameType, newNameType) { Function { pair -> pair.mapSecond { either -> either.map({ it.upgradeBlock() }, { it.ensureNamespaced().upgradeBlock() }) } } }
    }
}

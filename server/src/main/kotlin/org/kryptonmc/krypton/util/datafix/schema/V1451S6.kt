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

import com.mojang.datafixers.DSL.compoundList
import com.mojang.datafixers.DSL.constType
import com.mojang.datafixers.DSL.hook
import com.mojang.datafixers.DSL.intType
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.DSL.string
import com.mojang.datafixers.DSL.taggedChoiceLazy
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.Hook
import com.mojang.datafixers.types.templates.TypeTemplate
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Dynamic
import com.mojang.serialization.DynamicOps
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.util.datafix.References
import java.util.Optional
import java.util.function.Supplier

class V1451S6(versionKey: Int, parent: Schema?) : NamespacedSchema(versionKey, parent) {

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        val itemNameId: () -> TypeTemplate = { compoundList(References.ITEM_NAME.`in`(this), constType(intType())) }
        registerType(false, References.STATS) {
            optionalFields("stats", optionalFields(
                "minecraft:mined", compoundList(References.BLOCK_NAME.`in`(this)),
                "minecraft:crafted", itemNameId(),
                "minecraft:used", itemNameId(),
                "minecraft:broken", itemNameId(),
                "minecraft:picked_up", itemNameId(),
                optionalFields(
                    "minecraft:dropped", itemNameId(),
                    "minecraft:killed", compoundList(References.ENTITY_NAME.`in`(this), constType(intType())),
                    "minecraft:killed_by", compoundList(References.ENTITY_NAME.`in`(this), constType(intType())),
                    "minecraft:custom", compoundList(constType(NAMESPACED_STRING), constType(intType()))
                )
            ))
        }
        val criterionTypes = createCriterionTypes()
        registerType(false, References.OBJECTIVE) {
            hook(optionalFields("CriteriaType", taggedChoiceLazy("type", string(), criterionTypes)), UNPACK_OBJECTIVE_ID, REPACK_OBJECTIVE_ID)
        }
    }

    companion object {

        const val SPECIAL_OBJECTIVE_MARKER = "_special"
        val UNPACK_OBJECTIVE_ID = object : Hook.HookFunction {
            override fun <T> apply(ops: DynamicOps<T>, value: T): T {
                val dynamic = Dynamic(ops, value)
                return dynamic["CriteriaName"].asString().get().left().map {
                    val firstColon = it.indexOf(':')
                    if (firstColon < 0) return@map Pair.of(SPECIAL_OBJECTIVE_MARKER, it)
                    try {
                        val firstKey = Key.key(it.substring(0, firstColon), '.')
                        val secondKey = Key.key(it.substring(firstColon + 1), '.')
                        Pair.of(firstKey.asString(), secondKey.asString())
                    } catch (exception: InvalidKeyException) {
                        Pair.of(SPECIAL_OBJECTIVE_MARKER, it)
                    }
                }.map { dynamic.set("CriteriaType", dynamic.createMap(
                    mapOf(
                        dynamic.createString("type") to dynamic.createString(it.first),
                        dynamic.createString("id") to dynamic.createString(it.second)
                    )))
                }.orElse(dynamic).value
            }
        }
        val REPACK_OBJECTIVE_ID = object : Hook.HookFunction {
            override fun <T> apply(ops: DynamicOps<T>, value: T): T {
                val dynamic = Dynamic(ops, value)
                val data = dynamic["CriteriaType"].get().get().left().flatMap {
                    val optionalType = it["type"].asString().get().left()
                    val id = it["id"].asString().get().left()
                    if (optionalType.isPresent && id.isPresent) {
                        val type = optionalType.get()
                        Optional.of(if (type == SPECIAL_OBJECTIVE_MARKER) dynamic.createString(id.get()) else dynamic.createString("${type.packWithDot()}:${id.get().packWithDot()}"))
                    } else {
                        Optional.empty()
                    }
                }
                return data.map { dynamic.set("CriteriaName", it).remove("CriteriaType") }.orElse(dynamic).value
            }

            private fun String.packWithDot() = try {
                val key = Key.key(this)
                "${key.namespace()}.${key.value()}"
            } catch (exception: InvalidKeyException) {
                this
            }
        }
    }
}

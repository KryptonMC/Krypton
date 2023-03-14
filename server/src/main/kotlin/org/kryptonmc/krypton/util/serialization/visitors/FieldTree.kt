/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.util.serialization.visitors

import org.kryptonmc.nbt.TagType

class FieldTree(val depth: Int, val selectedFields: MutableMap<String, TagType<*>>, val fieldsToRecurse: MutableMap<String, FieldTree>) {

    private constructor(depth: Int) : this(depth, HashMap(), HashMap())

    fun addEntry(selector: FieldSelector) {
        if (depth <= selector.path.size) {
            fieldsToRecurse.computeIfAbsent(selector.path.get(depth - 1)) { FieldTree(depth + 1) }.addEntry(selector)
        } else {
            selectedFields.put(selector.name, selector.type)
        }
    }

    fun isSelected(type: TagType<*>, name: String): Boolean = type == selectedFields.get(name)

    companion object {

        @JvmStatic
        fun createRoot(): FieldTree = FieldTree(1)
    }
}

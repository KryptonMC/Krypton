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

import com.google.common.collect.ImmutableSet
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.TagType
import org.kryptonmc.nbt.visitor.StreamingTagVisitor
import java.util.ArrayDeque

class CollectFields(vararg selectors: FieldSelector) : CollectToTag() {

    private var fieldsToGetCount = selectors.size
    private val wantedTypes: Set<TagType<*>>
    private val stack = ArrayDeque<FieldTree>()

    init {
        val types = ImmutableSet.builder<TagType<*>>()
        val tree = FieldTree.createRoot()
        selectors.forEach {
            tree.addEntry(it)
            types.add(it.type)
        }
        stack.push(tree)
        types.add(CompoundTag.TYPE)
        wantedTypes = types.build()
    }

    override fun visitRootEntry(type: TagType<*>): StreamingTagVisitor.ValueResult {
        if (type != CompoundTag.TYPE) return StreamingTagVisitor.ValueResult.HALT
        return super.visitRootEntry(type)
    }

    override fun visitEntry(type: TagType<*>): StreamingTagVisitor.EntryResult {
        val tree = stack.element()
        if (depth() > tree.depth) return super.visitEntry(type)
        if (fieldsToGetCount <= 0) return StreamingTagVisitor.EntryResult.HALT
        return if (!wantedTypes.contains(type)) StreamingTagVisitor.EntryResult.SKIP else super.visitEntry(type)
    }

    override fun visitEntry(type: TagType<*>, name: String): StreamingTagVisitor.EntryResult {
        val tree = stack.element()
        if (depth() > tree.depth) return super.visitEntry(type, name)
        if (tree.selectedFields.remove(name, type)) {
            --fieldsToGetCount
            return super.visitEntry(type, name)
        }
        if (type == CompoundTag.TYPE) {
            val childTree = tree.fieldsToRecurse.get(name)
            if (childTree != null) {
                stack.push(childTree)
                return super.visitEntry(type, name)
            }
        }
        return StreamingTagVisitor.EntryResult.SKIP
    }

    override fun visitContainerEnd(): StreamingTagVisitor.ValueResult {
        if (depth() == stack.element().depth) stack.pop()
        return super.visitContainerEnd()
    }

    fun missingFieldCount(): Int = fieldsToGetCount
}

/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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

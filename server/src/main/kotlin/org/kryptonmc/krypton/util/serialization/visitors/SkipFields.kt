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

import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.TagType
import org.kryptonmc.nbt.visitor.StreamingTagVisitor
import java.util.ArrayDeque

class SkipFields(vararg selectors: FieldSelector) : CollectToTag() {

    private val stack = ArrayDeque<FieldTree>()

    init {
        val rootTree = FieldTree.createRoot()
        selectors.forEach { rootTree.addEntry(it) }
        stack.push(rootTree)
    }

    override fun visitEntry(type: TagType<*>, name: String): StreamingTagVisitor.EntryResult {
        val tree = stack.element()
        if (tree.isSelected(type, name)) return StreamingTagVisitor.EntryResult.SKIP
        if (type == CompoundTag.TYPE) {
            val childTree = tree.fieldsToRecurse.get(name)
            if (childTree != null) stack.push(childTree)
        }
        return super.visitEntry(type, name)
    }

    override fun visitContainerEnd(): StreamingTagVisitor.ValueResult {
        if (depth() == stack.element().depth) stack.pop()
        return super.visitContainerEnd()
    }
}

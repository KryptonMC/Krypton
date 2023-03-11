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

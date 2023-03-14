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

import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.MutableCompoundTag
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.TagType
import org.kryptonmc.nbt.visitor.StreamingTagVisitor
import java.util.ArrayDeque
import java.util.function.Consumer

@Suppress("MethodOverloading") // Inherited from supertype, not our problem.
open class CollectToTag : StreamingTagVisitor {

    private var lastName = ""
    private var rootTag: Tag? = null
    private val consumerStack = ArrayDeque<Consumer<Tag>>()

    fun result(): Tag? = rootTag

    protected fun depth(): Int = consumerStack.size

    protected fun appendEntry(tag: Tag) {
        consumerStack.last.accept(tag)
    }

    override fun visitEnd(): StreamingTagVisitor.ValueResult {
        appendEntry(EndTag.INSTANCE)
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visit(value: String): StreamingTagVisitor.ValueResult {
        appendEntry(StringTag.of(value))
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visit(value: Byte): StreamingTagVisitor.ValueResult {
        appendEntry(ByteTag.of(value))
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visit(value: Short): StreamingTagVisitor.ValueResult {
        appendEntry(ShortTag.of(value))
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visit(value: Int): StreamingTagVisitor.ValueResult {
        appendEntry(IntTag.of(value))
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visit(value: Long): StreamingTagVisitor.ValueResult {
        appendEntry(LongTag.of(value))
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visit(value: Float): StreamingTagVisitor.ValueResult {
        appendEntry(FloatTag.of(value))
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visit(value: Double): StreamingTagVisitor.ValueResult {
        appendEntry(DoubleTag.of(value))
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visit(value: ByteArray): StreamingTagVisitor.ValueResult {
        appendEntry(ByteArrayTag.of(value))
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visit(value: IntArray): StreamingTagVisitor.ValueResult {
        appendEntry(IntArrayTag.of(value))
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visit(value: LongArray): StreamingTagVisitor.ValueResult {
        appendEntry(LongArrayTag.of(value))
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visitList(type: TagType<*>, size: Int): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visitElement(type: TagType<*>, index: Int): StreamingTagVisitor.EntryResult {
        enterContainerIfNeeded(type)
        return StreamingTagVisitor.EntryResult.ENTER
    }

    override fun visitEntry(type: TagType<*>): StreamingTagVisitor.EntryResult = StreamingTagVisitor.EntryResult.ENTER

    override fun visitEntry(type: TagType<*>, name: String): StreamingTagVisitor.EntryResult {
        lastName = name
        enterContainerIfNeeded(type)
        return StreamingTagVisitor.EntryResult.ENTER
    }

    private fun enterContainerIfNeeded(type: TagType<*>) {
        if (type === ListTag.TYPE) {
            val list = MutableListTag.empty()
            appendEntry(list)
            consumerStack.addLast { list.add(it) }
        } else if (type === CompoundTag.TYPE) {
            val compound = MutableCompoundTag.empty()
            appendEntry(compound)
            consumerStack.addLast { compound.put(lastName, it) }
        }
    }

    override fun visitContainerEnd(): StreamingTagVisitor.ValueResult {
        consumerStack.removeLast()
        return StreamingTagVisitor.ValueResult.CONTINUE
    }

    override fun visitRootEntry(type: TagType<*>): StreamingTagVisitor.ValueResult {
        when (type) {
            ListTag.TYPE -> {
                val list = MutableListTag.empty()
                rootTag = list
                consumerStack.addLast { list.add(it) }
            }
            CompoundTag.TYPE -> {
                val compound = MutableCompoundTag.empty()
                rootTag = compound
                consumerStack.addLast { compound.put(lastName, it) }
            }
            else -> consumerStack.addLast { rootTag = it }
        }
        return StreamingTagVisitor.ValueResult.CONTINUE
    }
}

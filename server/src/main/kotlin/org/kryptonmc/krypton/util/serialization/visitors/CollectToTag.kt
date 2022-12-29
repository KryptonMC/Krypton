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

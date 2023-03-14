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
import org.kryptonmc.nbt.visitor.StreamingTagVisitor

@Suppress("MethodOverloading") // Inherited from supertype, not our problem.
object SkipAll : StreamingTagVisitor {

    override fun visitEnd(): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visit(value: String): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visit(value: Byte): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visit(value: Short): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visit(value: Int): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visit(value: Long): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visit(value: Float): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visit(value: Double): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visit(value: ByteArray): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visit(value: IntArray): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visit(value: LongArray): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visitList(type: TagType<*>, size: Int): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visitElement(type: TagType<*>, index: Int): StreamingTagVisitor.EntryResult = StreamingTagVisitor.EntryResult.SKIP

    override fun visitEntry(type: TagType<*>): StreamingTagVisitor.EntryResult = StreamingTagVisitor.EntryResult.SKIP

    override fun visitEntry(type: TagType<*>, name: String): StreamingTagVisitor.EntryResult = StreamingTagVisitor.EntryResult.SKIP

    override fun visitContainerEnd(): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE

    override fun visitRootEntry(type: TagType<*>): StreamingTagVisitor.ValueResult = StreamingTagVisitor.ValueResult.CONTINUE
}

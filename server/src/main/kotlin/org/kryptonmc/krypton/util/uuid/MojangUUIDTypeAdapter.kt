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
package org.kryptonmc.krypton.util.uuid

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.UUID

object MojangUUIDTypeAdapter : TypeAdapter<UUID>() {

    private val REPLACEMENT_REGEX = "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex()

    @JvmStatic
    fun fromString(string: String): UUID = UUID.fromString(string.replace(REPLACEMENT_REGEX, "$1-$2-$3-$4-$5"))

    @JvmStatic
    fun toString(uuid: UUID): String = uuid.toString().replace("-", "")

    override fun read(reader: JsonReader): UUID = fromString(reader.nextString())

    override fun write(out: JsonWriter, value: UUID?) {
        out.value(value?.let(MojangUUIDTypeAdapter::toString))
    }
}

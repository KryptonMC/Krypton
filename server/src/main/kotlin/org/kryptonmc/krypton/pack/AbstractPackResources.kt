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
package org.kryptonmc.krypton.pack

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import me.bardy.gsonkt.fromJson
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.util.logger
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.name

abstract class AbstractPackResources(protected val root: Path) : PackResources {

    override val name = root.name

    protected abstract fun resource(path: String): InputStream?

    protected abstract fun contains(path: String): Boolean

    protected fun metadata(stream: InputStream?): PackMetadata? {
        val json = try {
            stream?.reader()?.use { GSON.fromJson<JsonObject>(it) }
        } catch (exception: IOException) {
            LOGGER.error("Failed to load pack metadata!", exception)
            return null
        } catch (exception: JsonParseException) {
            LOGGER.error("Failed to parse pack metadata to JSON!", exception)
            return null
        }!!
        if (!json.has(PackMetadata.Serializer.name)) return null
        return try {
            PackMetadata.Serializer.fromJson(json[PackMetadata.Serializer.name].asJsonObject)
        } catch (exception: JsonParseException) {
            LOGGER.error("Failed to parse pack metadata to JSON!")
            return null
        }
    }

    protected fun warn(namespace: String) = LOGGER.warn("$namespace in $root must be lowercase! Ignoring...")

    override fun root(path: String): InputStream? {
        check("/" !in path && "\\" !in path) { "Root resources can only be file names!" }
        return resource(path)
    }

    override fun resource(key: Key) = resource("data/${key.namespace()}/${key.value()}")

    override fun contains(key: Key) = contains("data/${key.namespace()}/${key.value()}")

    override val metadata: PackMetadata?
        get() = resource(PackResources.PACK_META)?.use { metadata(it) }

    companion object {

        private val LOGGER = logger<AbstractPackResources>()
    }
}

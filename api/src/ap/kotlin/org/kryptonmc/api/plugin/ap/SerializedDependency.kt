package org.kryptonmc.api.plugin.ap

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

@JvmRecord
data class SerializedDependency(val id: String, val optional: Boolean) {

    companion object : TypeAdapter<SerializedDependency>() {

        override fun read(reader: JsonReader): SerializedDependency? {
            reader.beginObject()

            var id: String? = null
            var optional = false
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> id = reader.nextString()
                    "optional" -> optional = reader.nextBoolean()
                }
            }

            reader.endObject()
            if (id == null) return null
            return SerializedDependency(id, optional)
        }

        override fun write(writer: JsonWriter, value: SerializedDependency) {
            writer.beginObject()
            writer.name("id")
            writer.value(value.id)
            writer.name("optional")
            writer.value(value.optional)
            writer.endObject()
        }
    }
}

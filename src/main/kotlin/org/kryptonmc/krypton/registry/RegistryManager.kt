package org.kryptonmc.krypton.registry

import kotlinx.serialization.json.Json
import java.nio.charset.Charset

class RegistryManager {

    val registries: Registries

    init {
        val registriesFile = javaClass.classLoader.getResourceAsStream("registries.json")!!
            .reader(Charset.forName("UTF-8"))
            .readText()
        registries = JSON.decodeFromString(Registries.serializer(), registriesFile)
    }

    companion object {

        private val JSON = Json {}
    }
}
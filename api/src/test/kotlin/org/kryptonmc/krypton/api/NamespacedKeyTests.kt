package org.kryptonmc.krypton.api

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.kyori.adventure.key.Key
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toAdventureKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import kotlin.test.Test
import kotlin.test.assertEquals

class NamespacedKeyTests {

    private val key = NamespacedKey("krypton", "test")

    @Test
    fun `test namespaced key data retention`() {
        assertEquals("krypton", key.namespace)
        assertEquals("test", key.value)
        assertEquals("krypton", key.namespace())
        assertEquals("test", key.value())
    }

    @Test
    fun `test namespaced key string conversion`() {
        assertEquals("krypton:test", key.asString())
        assertEquals("krypton:test", key.toString())
    }

    @Test
    fun `test namespaced key invalid values`() {
        assertThrows<IllegalArgumentException> { "krypton::test".toNamespacedKey() }
        assertThrows<IllegalArgumentException> { "krypton&:test".toNamespacedKey() }
        assertThrows<IllegalArgumentException> { NamespacedKey("krypton&", "test") }
        assertThrows<IllegalArgumentException> { NamespacedKey("krypton:", "test") }
        assertThrows<IllegalArgumentException> { NamespacedKey("krypton", ":test") }
    }

    @Test
    fun `test namespaced key valid values`() {
        assertDoesNotThrow { "krypton:test".toNamespacedKey() }
        assertDoesNotThrow { NamespacedKey("krypton", "test") }
    }

    @Test
    fun `test conversion extensions`() {
        val adventure = Key.key("krypton:test")
        assertEquals(adventure, "krypton:test".toAdventureKey())
        assertEquals(NamespacedKey("krypton", "test"), adventure.toNamespacedKey())
    }

    @Test
    fun `test serialization of key`() {
        assertEquals("\"krypton:test\"", Json.encodeToString(key))
        assertEquals(key, Json.decodeFromString("\"krypton:test\""))
    }
}

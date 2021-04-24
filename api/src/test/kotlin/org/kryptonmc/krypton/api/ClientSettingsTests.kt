package org.kryptonmc.krypton.api

import org.kryptonmc.krypton.api.event.events.play.ClientSettingsEvent
import org.kryptonmc.krypton.api.event.events.play.SkinSettings
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class ClientSettingsTests {

    @Test
    fun `test client settings event data is retained`() {
        ClientSettingsEvent(player, Locale.ROOT, 10, true, ALL_OFF_SETTINGS).test(true)
        ClientSettingsEvent(player, Locale.ROOT, 10, false, ALL_OFF_SETTINGS).test(false)
    }

    @Test
    fun `test skin settings are retained`() {
        ALL_OFF_SETTINGS.test(false)
        ALL_ON_SETTINGS.test(true)
    }

    private fun ClientSettingsEvent.test(colours: Boolean) {
        assertEquals(org.kryptonmc.krypton.api.player, player)
        assertEquals(Locale.ROOT, locale)
        assertEquals(10, viewDistance)
        assertEquals(colours, hasColorsEnabled)
        assertEquals(ALL_OFF_SETTINGS, skinSettings)
    }

    private fun SkinSettings.test(expected: Boolean) {
        assertEquals(expected, cape)
        assertEquals(expected, jacket)
        assertEquals(expected, leftSleeve)
        assertEquals(expected, rightSleeve)
        assertEquals(expected, leftPants)
        assertEquals(expected, rightPants)
        assertEquals(expected, hat)
    }
}

private val ALL_OFF_SETTINGS = SkinSettings(cape = false, jacket = false, leftSleeve = false, rightSleeve = false, leftPants = false, rightPants = false, hat = false)
private val ALL_ON_SETTINGS = SkinSettings(cape = true, jacket = true, leftSleeve = true, rightSleeve = true, leftPants = true, rightPants = true, hat = true)

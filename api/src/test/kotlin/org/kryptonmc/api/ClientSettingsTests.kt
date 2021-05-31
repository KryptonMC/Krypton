/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:Suppress("BooleanLiteralArgument")
package org.kryptonmc.api

import org.kryptonmc.api.event.play.ClientSettingsEvent
import org.kryptonmc.api.event.play.SkinSettings
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
        assertEquals(org.kryptonmc.api.player, player)
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

private val ALL_OFF_SETTINGS = SkinSettings(false, false, false, false, false, false, false)
private val ALL_ON_SETTINGS = SkinSettings(true, true, true, true, true, true, true)

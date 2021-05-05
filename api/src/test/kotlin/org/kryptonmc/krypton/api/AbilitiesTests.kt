/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api

import org.kryptonmc.krypton.api.entity.Abilities
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AbilitiesTests {

    @Test
    fun `test abilities property retention with defaults`() {
        val abilities = Abilities()
        assertFalse(abilities.isInvulnerable)
        assertFalse(abilities.canFly)
        assertFalse(abilities.isFlying)
        assertTrue(abilities.canBuild)
        assertFalse(abilities.canInstantlyBuild)
        assertEquals(Abilities.DEFAULT_WALKING_SPEED, abilities.walkSpeed)
        assertEquals(Abilities.DEFAULT_FLYING_SPEED, abilities.flyingSpeed)
    }

    @Test
    fun `test abilities property retention with booleans true`() {
        val abilities = Abilities(isInvulnerable = true, canFly = true, isFlying = true, canBuild = false, canInstantlyBuild = true)
        assertTrue(abilities.isInvulnerable)
        assertTrue(abilities.canFly)
        assertTrue(abilities.isFlying)
        assertFalse(abilities.canBuild)
        assertTrue(abilities.canInstantlyBuild)
        assertEquals(Abilities.DEFAULT_WALKING_SPEED, abilities.walkSpeed)
        assertEquals(Abilities.DEFAULT_FLYING_SPEED, abilities.flyingSpeed)
    }
}

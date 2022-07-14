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
package org.kryptonmc.krypton

import io.mockk.every
import io.mockk.mockk
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.TextColor
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.util.Color
import org.kryptonmc.krypton.effect.particle.KryptonParticleEffect
import org.kryptonmc.krypton.effect.particle.data.KryptonNoteParticleData
import org.kryptonmc.krypton.util.Bootstrap
import org.spongepowered.math.vector.Vector3d
import java.awt.Color as AwtColor
import kotlin.test.Test
import kotlin.test.assertEquals

class ParticleEffectTests {

    @Test
    fun `test bounds`() {
        val particle = mockk<ParticleType> {
            every { key() } returns Key.key("krypton", "test")
        }
        assertThrows<IllegalArgumentException> { KryptonParticleEffect(particle, -1, Vector3d.ZERO, false) }
        assertThrows<IllegalArgumentException> { KryptonParticleEffect(particle, 0, Vector3d.ZERO, false) }
        assertThrows<IllegalArgumentException> { KryptonNoteParticleData(100) }
        assertDoesNotThrow { KryptonNoteParticleData(0) }
        assertDoesNotThrow { KryptonNoteParticleData(24) }
    }

    @Test
    fun `test color values are cut off and disallowed in color builder`() {
        Bootstrap.preload()
        val dust = ParticleTypes.DUST.builder()
            .quantity(1)
            .longDistance(true)
            .offset(Vector3d.RIGHT)

        // Check HSB values with any above 1 are thrown out
        assertThrows<IllegalArgumentException> { dust.hsv(1.2F, 0.5F, 0.3F) }
        assertThrows<IllegalArgumentException> { dust.hsv(-0.2F, 0.5F, 0.3F) }
        assertThrows<IllegalArgumentException> { dust.hsv(0.5F, 1.2F, 0.3F) }
        assertThrows<IllegalArgumentException> { dust.hsv(0.5F, -0.2F, 0.3F) }
        assertThrows<IllegalArgumentException> { dust.hsv(0.5F, 0.3F, 1.2F) }
        assertThrows<IllegalArgumentException> { dust.hsv(0.5F, 0.3F, -0.2F) }

        // Check HSV conversion matches up with Color.HSBToRGB
        val colorDataFromHSV = dust.hsv(1F, 0.5F, 0.3F).build().data as ColorParticleData
        val hsvToRGB = AwtColor.HSBtoRGB(1F, 0.5F, 0.3F)
        assertEquals(colorDataFromHSV.red, hsvToRGB shr 16 and 0xFF)
        assertEquals(colorDataFromHSV.green, hsvToRGB shr 8 and 0xFF)
        assertEquals(colorDataFromHSV.blue, hsvToRGB and 0xFF)

        // Check that colour set from RGB matches up with colour set from java.awt.Color
        val colorDataFromRGB = dust.rgb(AwtColor.ORANGE.rgb).build().data as ColorParticleData
        val colorDataFromColor = dust.color(Color.of(AwtColor.ORANGE.rgb)).build().data as ColorParticleData
        assertEquals(colorDataFromRGB.red, colorDataFromColor.red)
        assertEquals(colorDataFromRGB.green, colorDataFromColor.green)
        assertEquals(colorDataFromRGB.blue, colorDataFromColor.blue)

        // Check that RGB values above 255 are cut off
        val colorDataFromValues = dust.rgb(287, 200, 132).build().data as ColorParticleData
        assertEquals(287 and 0xFF, colorDataFromValues.red)
        val colorDataFromRGBLike = dust.rgb(TextColor.color(287, 200, 132)).build().data as ColorParticleData
        assertEquals(287 and 0xFF, colorDataFromRGBLike.red)
    }

    @Test
    fun `test note disallows values outside of range`() {
        val note = ParticleTypes.NOTE.builder()
        assertThrows<IllegalArgumentException> { note.note(25) }
        assertThrows<IllegalArgumentException> { note.note(-1) }
    }
}

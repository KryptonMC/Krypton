package org.kryptonmc.krypton.api

import org.kryptonmc.krypton.api.effect.particle.BlockParticle
import org.kryptonmc.krypton.api.effect.particle.ColorParticle
import org.kryptonmc.krypton.api.effect.particle.DirectionalParticle
import org.kryptonmc.krypton.api.effect.particle.DustParticle
import org.kryptonmc.krypton.api.effect.particle.ItemParticle
import org.kryptonmc.krypton.api.effect.particle.NoteParticle
import org.kryptonmc.krypton.api.effect.particle.Particle
import org.kryptonmc.krypton.api.effect.particle.ParticleType
import org.kryptonmc.krypton.api.effect.particle.SimpleParticle
import org.kryptonmc.krypton.api.registry.NamespacedKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private val KEY = NamespacedKey("krypton", "test")

class ParticleTests {

    @Test
    fun `test simple particle holds data correctly`() = SimpleParticle(KEY, 100).test()

    @Test
    fun `test directional particle holds data correctly`() = DirectionalParticle(KEY, 100).test()

    @Test
    fun `test block particle holds data correctly`() = BlockParticle(KEY, 100).test()

    @Test
    fun `test item particle holds data correctly`() = ItemParticle(KEY, 100).test()

    @Test
    fun `test color particle holds data correctly`() = ColorParticle(KEY, 100).test()

    @Test
    fun `test dust particle holds data correctly`() = DustParticle(KEY, 100).test()

    @Test
    fun `test note particle holds data correctly`() = NoteParticle(KEY, 100).test()

    @Test
    fun `test particle type list contains entries`() = assertTrue(ParticleType.values.isNotEmpty())

    @Test
    fun `test all particle type entry ids`() {
        var id = 0
        ParticleType.values.forEachIndexed { index, it ->
            assertNotNull(it)
            assertEquals(id, index)
            assertEquals(id, it.id)
            id++
        }
    }

    private fun Particle.test() {
        assertEquals(KEY, key)
        assertEquals(100, id)
    }
}

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
package org.kryptonmc.krypton.util

object Bootstrap {

    @Volatile
    private var bootstrapped = false

    fun init() {
        if (bootstrapped) return
        bootstrapped = true

        // Preload all the registry classes to ensure everything is properly registered
        Class.forName("org.kryptonmc.api.registry.RegistryRoots")
        Class.forName("org.kryptonmc.api.registry.RegistryKeys")
        Class.forName("org.kryptonmc.api.registry.Registries")
        Class.forName("org.kryptonmc.api.effect.particle.ParticleType")
        Class.forName("org.kryptonmc.api.effect.sound.SoundTypes")
        Class.forName("org.kryptonmc.api.entity.EntityTypes")
        Class.forName("org.kryptonmc.api.item.ItemTypes")

        // Preload the old registry
        Class.forName("org.kryptonmc.krypton.registry.FileRegistries")

        // Preload some other frequently used objects so they aren't loaded on first player join
        Class.forName("org.kryptonmc.krypton.registry.tags.TagManager")
        Class.forName("org.kryptonmc.krypton.world.block.palette.GlobalPalette")
        Class.forName("org.kryptonmc.krypton.command.argument.ArgumentTypes")
    }
}

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
package org.kryptonmc.krypton.adventure
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.flattener.ComponentFlattener
import org.kryptonmc.krypton.util.TranslationBootstrap
import java.util.Locale

/**
 * Various things used by Krypton for supporting Adventure.
 */
object KryptonAdventure {

    /**
     * This flattener adds the use of the official Minecraft translations from en_us.json
     * to render translatable components when they are flattened.
     */
    @JvmField
    val FLATTENER: ComponentFlattener = ComponentFlattener.basic().toBuilder()
        .complexMapper<TranslatableComponent> { translatable, mapper ->
            mapper(TranslationBootstrap.RENDERER.render(translatable, Locale.ENGLISH))
        }
        .build()
}

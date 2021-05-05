/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util

import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import java.text.MessageFormat
import java.util.Locale

/**
 * Register console locale strings. This is temporary, and will be replaced with a proper
 * locale system when Krypton has one.
 */
object TranslationRegister {

    private val registry = TranslationRegistry.create(Key.key("krypton", "console"))

    fun initialize() {
        registry.register("multiplayer.player.joined", Locale.ENGLISH, MessageFormat("{0} joined the game"))
        registry.register("multiplayer.player.left", Locale.ENGLISH, MessageFormat("{0} left the game"))
        registry.register("chat.type.text", Locale.ENGLISH, MessageFormat("<{0}> {1}"))
        GlobalTranslator.get().addSource(registry)
    }
}

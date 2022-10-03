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

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.util.function.Consumer

class KryptonLegacyComponentSerializerProvider : LegacyComponentSerializer.Provider {

    override fun legacy(): Consumer<LegacyComponentSerializer.Builder> = Consumer {}

    override fun legacyAmpersand(): LegacyComponentSerializer = createSerializer(LegacyComponentSerializer.AMPERSAND_CHAR)

    override fun legacySection(): LegacyComponentSerializer = createSerializer(LegacyComponentSerializer.SECTION_CHAR)

    private fun createSerializer(char: Char): LegacyComponentSerializer = LegacyComponentSerializer.builder()
        .character(char)
        .hexCharacter(LegacyComponentSerializer.HEX_CHAR)
        .flattener(KryptonAdventure.FLATTENER)
        .build()
}

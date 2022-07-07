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
package org.kryptonmc.generators

import java.lang.reflect.Field

/**
 * This class exists to bridge the gap between actual fields and synthetic
 * fields.
 *
 * When generating sound events, we need to account for the really annoying
 * "GOAT_HORN_SOUND_VARIANTS" field, which is a generated list of sound events.
 * What we do with this is collect all the normal fields, then get that
 * specific list and turn it in to a list of collected fields, all with names
 * like "GOAT_HORN_SOUND_VARIANT_{index}" and the event as the value.
 */
@JvmRecord
data class CollectedField(val name: String, val value: Any) {

    constructor(field: Field) : this(field.name, field.get(null))
}

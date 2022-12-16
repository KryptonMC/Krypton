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
package org.kryptonmc.internal.annotations

import kotlin.reflect.KClass

/**
 * Indicates that the annotated type is a catalogue type, usually meaning it
 * provides access to many static instances of the given catalogued [type].
 *
 * All types contained within this API that are annotated with this
 * annotation will meet certain requirements that are listed below, and it is
 * also highly recommended that users of this annotation also meet the same
 * requirements, as follows:
 * - The values made available within this type must remain constant for
 * the entire runtime.
 * - All values must not be references to null, or any other types that could
 * cause ambiguity or annoyance for users of the API.
 *
 * In addition, it is possible for two constant values to point to the same
 * object, for the purpose of creating aliases.
 *
 * Furthermore, not all catalogue types are exhaustive, there may be more
 * instances of the catalogued type available. Within the scope of this API,
 * all of the constants listed in the catalogue type will always be an
 * exhaustive list of all of the known constants that are guaranteed to always
 * be available.
 *
 * @param type the type the target is a catalogue of
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
public annotation class Catalogue(public val type: KClass<*>)

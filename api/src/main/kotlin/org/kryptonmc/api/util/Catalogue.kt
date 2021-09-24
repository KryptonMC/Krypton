/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * Marker annotation indicating that the annotated type is a catalogue type,
 * usually meaning it provides access to many static instances of the given
 * catalogued [type].
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
@Inherited
@MustBeDocumented
// Also, for all of you americans out there, it's "catalogue", not "catalog",
// that's why this annotation is named as such. This is one of the spellings
// I won't budge on.
public annotation class Catalogue(public vararg val type: KClass<*>)

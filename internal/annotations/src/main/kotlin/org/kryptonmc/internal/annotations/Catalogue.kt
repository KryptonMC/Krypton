/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

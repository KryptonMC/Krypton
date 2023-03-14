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
data class CollectedField<T>(val name: String, val value: T) {

    constructor(field: Field, type: Class<T>) : this(field.name, type.cast(field.get(null)))
}

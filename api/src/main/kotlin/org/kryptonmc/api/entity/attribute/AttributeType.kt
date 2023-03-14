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
package org.kryptonmc.api.entity.attribute

import net.kyori.adventure.key.Keyed
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * The type of an attribute.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(AttributeTypes::class)
@ImmutableType
public interface AttributeType : Translatable, Keyed {

    /**
     * The default value for attributes of this type.
     */
    @get:JvmName("defaultValue")
    public val defaultValue: Double

    /**
     * Ensures that the given [value] satisfies the constraints of this
     * attribute type.
     *
     * For example, with ranged attribute types, this will ensure that the
     * value is between the minimum and maximum value.
     *
     * @param value the value to sanitize
     * @return the sanitized result
     */
    public fun sanitizeValue(value: Double): Double
}

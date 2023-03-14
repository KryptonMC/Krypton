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
package org.kryptonmc.krypton.entity.attribute

import org.kryptonmc.api.entity.attribute.RangedAttributeType
import org.kryptonmc.krypton.util.math.Maths

class KryptonRangedAttributeType(
    defaultValue: Double,
    sendToClient: Boolean,
    translationKey: String,
    override val minimum: Double,
    override val maximum: Double
) : KryptonAttributeType(defaultValue, sendToClient, translationKey), RangedAttributeType {

    init {
        require(minimum <= maximum) { "Minimum value cannot be larger than maximum value!" }
        require(defaultValue >= minimum) { "Default value cannot be less than minimum value!" }
        require(defaultValue <= maximum) { "Default value cannot be greater than maximum value!" }
    }

    override fun sanitizeValue(value: Double): Double = if (value.isNaN()) minimum else Maths.clamp(value, minimum, maximum)
}

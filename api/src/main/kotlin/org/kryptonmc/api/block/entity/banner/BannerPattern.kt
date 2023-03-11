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
package org.kryptonmc.api.block.entity.banner

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * A pattern for a banner. These are immutable, and may be reused.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface BannerPattern {

    /**
     * The type of this banner pattern.
     */
    @get:JvmName("type")
    public val type: BannerPatternType

    /**
     * The colour of this banner pattern.
     */
    @get:JvmName("color")
    public val color: DyeColor

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun of(type: BannerPatternType, color: DyeColor): BannerPattern
    }

    public companion object {

        /**
         * Creates a new banner pattern with the given values.
         *
         * @param type the type
         * @param color the colour
         * @return a new banner pattern
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(type: BannerPatternType, color: DyeColor): BannerPattern = Krypton.factory<Factory>().of(type, color)
    }
}

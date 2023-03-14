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
package org.kryptonmc.api.entity.player

import org.kryptonmc.internal.annotations.ImmutableType

/**
 * The parts of the skin that are shown on a player.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface SkinParts {

    /**
     * Whether the cape is shown.
     *
     * @return true if the cape is shown
     */
    public fun hasCape(): Boolean

    /**
     * Whether the jacket is shown.
     *
     * @return true if the jacket is shown
     */
    public fun hasJacket(): Boolean

    /**
     * Whether the left sleeve is shown.
     *
     * @return true if the left sleeve is shown
     */
    public fun hasLeftSleeve(): Boolean

    /**
     * Whether the right sleeve is shown.
     *
     * @return true if the right sleeve is shown
     */
    public fun hasRightSleeve(): Boolean

    /**
     * Whether the left half of the pants is shown.
     *
     * @return true if the left pants are shown
     */
    public fun hasLeftPants(): Boolean

    /**
     * Whether the right half of the pants is shown.
     *
     * @return true if the right pants are shown
     */
    public fun hasRightPants(): Boolean

    /**
     * Whether the hat is shown.
     *
     * @return true if the hat is shown
     */
    public fun hasHat(): Boolean
}

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
package org.kryptonmc.api.block.meta

/**
 * Indicates the tilt of a big dripleaf this property is applied to.
 *
 * Large dripleaf plants can tilt downwards when significant force is applied
 * to the top of them, for example, when a player stands on them.
 */
public enum class Tilt {

    /**
     * The dripleaf is flat, and not tilting.
     */
    NONE,

    /**
     * The dripleaf is unstable, and will partially tilt after 10 ticks.
     *
     * This tilt state does not cause sculk vibrations.
     */
    UNSTABLE,

    /**
     * The dripleaf is partially tilted, and will fully tilt after 10 ticks.
     */
    PARTIAL,

    /**
     * The dripleaf will fully tilt, and will return to none after 100 ticks.
     */
    FULL;

    /**
     * Gets whether this tilt causes vibrations that sculk sensors in the area
     * will respond to.
     *
     * This determines whether jumping on a big dripleaf with this tilt will
     * alert nearby sculk sensors.
     *
     * @return true if causes vibrations, false otherwise
     */
    public fun causesVibrations(): Boolean = this != UNSTABLE
}

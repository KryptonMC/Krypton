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
package org.kryptonmc.krypton.packet.out.play

/**
 * An integer pseudo-enum holding all the entity animations.
 *
 * This is not an enum to avoid having to lookup from enum values, and also, because we can accept any value, the client should just ignore it.
 * Also, depending on the ordinal of an enum is not recommended.
 */
object EntityAnimations {

    const val SWING_MAIN_ARM: Int = 0
    const val TAKE_DAMAGE: Int = 1
    const val LEAVE_BED: Int = 2
    const val SWING_OFFHAND: Int = 3
    const val CRITICAL_EFFECT: Int = 4
    const val MAGIC_CRITICAL_EFFECT: Int = 5
}

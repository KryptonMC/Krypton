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

object GameEventTypes {

    const val NO_RESPAWN_BLOCK_AVAILABLE: Byte = 0
    const val END_RAINING: Byte = 1
    const val BEGIN_RAINING: Byte = 2
    const val CHANGE_GAMEMODE: Byte = 3
    const val WIN_GAME: Byte = 4
    const val DEMO_EVENT: Byte = 5
    const val ARROW_HIT_PLAYER: Byte = 6
    const val RAIN_LEVEL_CHANGE: Byte = 7
    const val THUNDER_LEVEL_CHANGE: Byte = 8
    const val PLAY_PUFFERFISH_STING_SOUND: Byte = 9
    const val PLAY_ELDER_GUARDIAN_MOB_APPEARANCE: Byte = 10
    const val ENABLE_RESPAWN_SCREEN: Byte = 11
}

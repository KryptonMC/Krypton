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
package org.kryptonmc.krypton.world.components

import org.kryptonmc.api.util.Vec3i

// TODO: Revise when lighting engine is implemented
interface BrightnessGetter : BlockGetter {

    fun skyDarken(): Int

    fun getSkyBrightness(x: Int, y: Int, z: Int): Int = 0

    fun getSkyBrightness(pos: Vec3i): Int = getSkyBrightness(pos.x, pos.y, pos.z)

    fun getBlockBrightness(x: Int, y: Int, z: Int): Int = 0

    fun getBlockBrightness(pos: Vec3i): Int = getBlockBrightness(pos.x, pos.y, pos.z)

    fun canSeeSky(x: Int, y: Int, z: Int): Boolean = getSkyBrightness(x, y, z) >= maximumLightLevel()

    fun canSeeSky(pos: Vec3i): Boolean = canSeeSky(pos.x, pos.y, pos.z)
}

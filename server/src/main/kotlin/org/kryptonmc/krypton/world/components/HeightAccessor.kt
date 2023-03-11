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
import org.kryptonmc.krypton.coordinate.SectionPos

interface HeightAccessor {

    fun height(): Int

    fun minimumBuildHeight(): Int

    fun maximumBuildHeight(): Int = minimumBuildHeight() + height()

    fun minimumSection(): Int = SectionPos.blockToSection(minimumBuildHeight())

    fun maximumSection(): Int = SectionPos.blockToSection(maximumBuildHeight() - 1) + 1

    fun sectionCount(): Int = maximumSection() - minimumSection()

    fun getSectionIndex(y: Int): Int = getSectionIndexFromSectionY(SectionPos.blockToSection(y))

    fun getSectionIndexFromSectionY(y: Int): Int = y - minimumSection()

    fun getSectionYFromSectionIndex(index: Int): Int = index + minimumSection()

    fun isOutsideBuildHeight(pos: Vec3i): Boolean = isOutsideBuildHeight(pos.y)

    fun isOutsideBuildHeight(y: Int): Boolean = y < minimumBuildHeight() || y >= maximumBuildHeight()
}

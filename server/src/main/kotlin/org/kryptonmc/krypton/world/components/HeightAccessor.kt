/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.components

import org.kryptonmc.krypton.coordinate.BlockPos
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

    fun isOutsideBuildHeight(pos: BlockPos): Boolean = isOutsideBuildHeight(pos.y)

    fun isOutsideBuildHeight(y: Int): Boolean = y < minimumBuildHeight() || y >= maximumBuildHeight()
}

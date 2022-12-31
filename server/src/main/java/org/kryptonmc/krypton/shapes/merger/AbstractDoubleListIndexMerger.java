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
package org.kryptonmc.krypton.shapes.merger;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;

/*
 * This hacky class allows us to keep size being a method on IndexMerger and avoids us running in to conflicts when implementing
 * NonOverlappingMerger.
 *
 * The problem is that Kotlin makes fake overrides of java's collection methods, specifically the size method becomes a size property,
 * which conflicts with our size method on IndexMerger.
 */
public abstract class AbstractDoubleListIndexMerger extends AbstractDoubleList implements IndexMerger {

    @Override
    public abstract int size();
}

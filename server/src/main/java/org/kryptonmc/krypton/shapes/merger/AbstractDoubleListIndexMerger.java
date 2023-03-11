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

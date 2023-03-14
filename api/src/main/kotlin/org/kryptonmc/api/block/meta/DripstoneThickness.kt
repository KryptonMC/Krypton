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
 * Indicates the thickness of a dripstone block that forms the multi block
 * stalagmites and stalactites.
 *
 * As stalagmites and stalactites are spikes on the ceiling or the ground,
 * and they vary in size, it is important to be able to track which part of it
 * a specific block is. For example, a three block stalagmite may have a base,
 * the part connected to the ground, a middle, the section between the base
 * and the top, and the tip, the section at the top that ends the stalagmite.
 */
public enum class DripstoneThickness {

    TIP_MERGE,
    TIP,
    FRUSTUM,
    MIDDLE,
    BASE
}

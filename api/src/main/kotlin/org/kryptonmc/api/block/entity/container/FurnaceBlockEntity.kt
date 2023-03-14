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
package org.kryptonmc.api.block.entity.container

import org.kryptonmc.api.block.entity.NameableBlockEntity

/**
 * A furnace block entity.
 */
public interface FurnaceBlockEntity : ContainerBlockEntity, NameableBlockEntity {

    /**
     * The amount of time, in ticks, until the fuel in the furnace runs out.
     */
    public var remainingFuel: Int

    /**
     * The total amount of time, in ticks, that the fuel in the furnace will
     * burn for.
     */
    public val fuelDuration: Int

    /**
     * The current progress of the item being cooked.
     */
    public var cookingProgress: Int

    /**
     * The amount of time, in ticks, until the item in the furnace is cooked.
     */
    public val cookingDuration: Int
}

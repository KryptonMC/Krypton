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
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.entity.animal.Bee
import org.kryptonmc.api.util.Vec3i

/**
 * A beehive.
 */
public interface Beehive : EntityStorageBlockEntity<Bee> {

    /**
     * The position of a flower that one of the bees has found, so that other
     * bees in the beehive can find it.
     */
    public var flower: Vec3i?

    /**
     * Whether this beehive is sedated due to a campfire underneath it.
     *
     * @return true if this beehive is sedated
     */
    public fun isSedated(): Boolean
}

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

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.World

/**
 * A block entity is an entity that exists as a companion to a block, so that
 * block can store information that would violate the functionality of blocks
 * on their own.
 *
 * Despite the name, however, these do not behave anything like regular
 * entities. They do not actually exist to the end user, cannot be seen, and
 * only exist to hold data that blocks cannot.
 *
 * These used to be known as tile entities, for all of you folks who remember
 * those days.
 */
public interface BlockEntity {

    /**
     * The type of this block entity.
     */
    public val type: BlockEntityType<*>

    /**
     * The world this block entity is in.
     */
    public val world: World

    /**
     * The block that this entity is bound to.
     */
    public val block: Block

    /**
     * The position of this block entity.
     *
     * This will be identical to the position of the associated block.
     */
    public val position: Vec3i
}

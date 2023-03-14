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
package org.kryptonmc.api.event.player.interact

import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i

/**
 * Called when a player places a block.
 */
public interface PlayerPlaceBlockEvent : PlayerInteractEvent {

    /**
     * The block that was placed.
     */
    public val block: BlockState

    /**
     * The hand that the player used to place the block.
     */
    public val hand: Hand

    /**
     * The position where the block was placed.
     */
    public val position: Vec3i

    /**
     * The face of the block on which the block was placed.
     */
    public val face: Direction

    /**
     * Whether the player's head is inside the block.
     */
    public val isInsideBlock: Boolean
}

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
package org.kryptonmc.api.entity

import org.kryptonmc.api.entity.player.Player

/**
 * An orb of experience.
 */
public interface ExperienceOrb : Entity {

    /**
     * The remaining amount of times this orb can be picked up.
     *
     * When this orb is picked up, this value will decrease by 1.
     * When multiple orbs are merged, their counts will be summed.
     * When this value reaches 0, the orb is depleted.
     */
    public val count: Int

    /**
     * The current health of the orb.
     *
     * Experience orbs can take damage from fire, lava, falling anvils, and
     * explosions.
     * The orb is destroyed when this value reaches 0.
     */
    public val health: Int

    /**
     * The amount of experience given by this orb when it is picked up.
     */
    public val experience: Int

    /**
     * The player this orb is currently following, or null if this orb is not
     * currently following a player.
     */
    public val following: Player?
}

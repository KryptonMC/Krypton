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
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.text.Component

/**
 * The score of a member of a team for a specific objective.
 */
public interface Score : ScoreboardBound {

    /**
     * The name of the score.
     */
    public val name: Component

    /**
     * The objective this score is registered to, or null if this score is not
     * currently registered to an objective.
     */
    public val objective: Objective?

    /**
     * The underlying value of this score.
     */
    public var score: Int

    /**
     * If this score is locked, meaning it cannot be changed.
     */
    public var isLocked: Boolean
}

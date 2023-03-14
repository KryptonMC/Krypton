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
 * Something that may be a member of a team, having a meaningful representation
 * on a team.
 */
public interface TeamMember {

    /**
     * If the member is currently part of a team, the team the member is part
     * of.
     */
    public val team: Team?

    /**
     * How this member will be represented in a team.
     *
     * For entities, this is generally a [text component][Component.text]
     * containing their UUID, with the exception of the player, for which it is
     * their username.
     */
    public val teamRepresentation: Component
}

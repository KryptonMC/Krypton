/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.text.Component

/**
 * Something that may be a member of a team, having a meaningful representation
 * on a team.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface TeamMember {

    /**
     * If the member is currently part of a team, the team the member is part
     * of.
     */
    @get:JvmName("team")
    public val team: Team?

    /**
     * How this member will be represented in a team.
     *
     * For entities, this is generally a [text component][Component.text]
     * containing their UUID, with the exception of the player, for which it is
     * their username.
     */
    @get:JvmName("teamRepresentation")
    public val teamRepresentation: Component
}

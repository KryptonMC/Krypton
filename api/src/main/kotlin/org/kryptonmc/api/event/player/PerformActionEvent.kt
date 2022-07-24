/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.event.GenericResult
import org.kryptonmc.api.event.ResultedEvent

/**
 * Called when the given [player] performs the given [action].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface PerformActionEvent : PlayerEvent, ResultedEvent<GenericResult> {

    /**
     * The action that is being performed by the player.
     */
    @get:JvmName("action")
    public val action: Action

    /**
     * Actions that a player may take.
     */
    public enum class Action {

        START_SNEAKING,
        STOP_SNEAKING,
        LEAVE_BED,
        START_SPRINTING,
        STOP_SPRINTING,
        START_JUMP_WITH_HORSE,
        STOP_JUMP_WITH_HORSE,
        OPEN_HORSE_INVENTORY,
        START_FLYING_WITH_ELYTRA,
        STOP_FLYING_WITH_ELYTRA
    }
}

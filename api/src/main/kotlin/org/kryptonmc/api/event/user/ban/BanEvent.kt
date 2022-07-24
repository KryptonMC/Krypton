/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.user.ban

import org.kryptonmc.api.event.GenericResult
import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.api.user.ban.Ban

/**
 * An event that involves a ban. This will either be a ban being issued or a
 * ban being repealed (a pardon).
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public sealed interface BanEvent<B : Ban> : ResultedEvent<GenericResult> {

    /**
     * The ban that was issued or repealed.
     */
    @get:JvmName("ban")
    public val ban: B
}

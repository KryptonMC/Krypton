/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.user.whitelist

import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.event.GenericResult
import org.kryptonmc.api.event.ResultedEvent

/**
 * Called when a whitelisted game profile is removed from the whitelist.
 *
 * @param profile the whitelisted profile
 */
public class RemoveWhitelistedProfileEvent(public val profile: GameProfile) : ResultedEvent<GenericResult> {

    override var result: GenericResult = GenericResult.allowed()
}

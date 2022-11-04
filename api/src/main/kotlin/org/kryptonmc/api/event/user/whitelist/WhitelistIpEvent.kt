/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.user.whitelist

import org.kryptonmc.api.event.GenericResult
import org.kryptonmc.api.event.ResultedEvent
import java.net.InetAddress

/**
 * Called when an IP address is whitelisted.
 */
public interface WhitelistIpEvent : ResultedEvent<GenericResult> {

    /**
     * The whitelisted IP address.
     */
    public val address: InetAddress
}

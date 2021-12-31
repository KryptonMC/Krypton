/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
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
 *
 * @param address the whitelisted IP address
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public class WhitelistIpEvent(@get:JvmName("address") public val address: InetAddress) : ResultedEvent<GenericResult> {

    @get:JvmName("result")
    override var result: GenericResult = GenericResult.allowed()
}

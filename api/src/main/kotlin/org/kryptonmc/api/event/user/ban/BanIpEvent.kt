/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.user.ban

import org.kryptonmc.api.user.ban.Ban

/**
 * Called when an IP is banned.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BanIpEvent : BanEvent<Ban.IP>

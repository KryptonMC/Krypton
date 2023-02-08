/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.annotation

/**
 * A marker annotation that signals that an event is highly sensitive, and
 * should be handled very quickly. If heavy processing needs to be done for one
 * of these events, it should be done asynchronously.
 *
 * These events could be sensitive for a number of reasons. For example, an
 * event like [org.kryptonmc.api.event.player.PlayerMoveEvent] is sensitive because
 * it is fired very frequently, even if the server only has a couple of
 * players. Tick start and tick end events are also called a lot, and your
 * processing could severely limit the TPS capability of the server.
 */
public annotation class PerformanceSensitive

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

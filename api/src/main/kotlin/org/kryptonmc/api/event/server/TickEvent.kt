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
package org.kryptonmc.api.event.server

import org.kryptonmc.api.event.Event

/**
 * An event that is called when the server starts or ends a tick.
 *
 * These events are called incredibly frequently. On a server with a normal
 * tick speed, these events will be called 20 times per second.
 */
public sealed interface TickEvent : Event {

    /**
     * The number of the tick that has started. This will start from 0, which
     * will be the first tick, and increment by 1 for every completed tick
     * while the server is running.
     *
     * This is NOT a persisted value. It only counts up when the server is
     * running. When the server is restarted, this will reset to 0.
     */
    public val tickNumber: Int
}

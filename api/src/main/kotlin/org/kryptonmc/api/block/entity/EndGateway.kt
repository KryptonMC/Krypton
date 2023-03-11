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
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.util.Vec3i

/**
 * An end gateway.
 */
public interface EndGateway : BlockEntity {

    /**
     * The position that a player will be teleported to when they enter this
     * gateway.
     */
    public var exitPosition: Vec3i

    /**
     * Whether this gateway will teleport a player to the exact exit position.
     *
     * If this is false, the gateway will attempt to find the closest possible
     * safe exit location to the exit position.
     */
    public var isExactTeleport: Boolean

    /**
     * The age, in ticks, of this gateway.
     *
     * If this age is less than 200 ticks, the beam will be magenta.
     * If this age is a multiple of 2400 ticks, the beam will be purple.
     */
    public var age: Int
}

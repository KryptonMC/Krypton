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

import net.kyori.adventure.text.Component

/**
 * A command block.
 */
public interface CommandBlock : BlockEntity {

    /**
     * The command that this command block will run.
     */
    public var command: String

    /**
     * The last output from the command block running the command.
     */
    public var lastOutput: Component

    /**
     * Whether this command block is powered.
     *
     * @return true if this command block is powered
     */
    public fun isPowered(): Boolean

    /**
     * Whether this command block executes automatically, not requiring a
     * redstone signal.
     *
     * @return true if this command block executes automatically
     */
    public fun isAutomatic(): Boolean

    /**
     * Executes this command block.
     */
    public fun execute()
}

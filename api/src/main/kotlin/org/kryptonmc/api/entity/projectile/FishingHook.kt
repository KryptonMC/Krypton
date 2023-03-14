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
package org.kryptonmc.api.entity.projectile

import org.kryptonmc.api.entity.Entity

/**
 * A fishing hook on the end of a fishing rod.
 */
public interface FishingHook : Projectile {

    /**
     * The entity that is currently hooked by this hook, or null if this hook
     * is not hooking an entity.
     */
    public val hooked: Entity?

    /**
     * The current state of this hook.
     */
    public val state: State

    /**
     * If the hooked entity is biting the hook, meaning it can be caught.
     *
     * @return true if the hooked entity is biting the hook
     */
    public fun isBiting(): Boolean

    /**
     * The current state of a fishing hook.
     */
    public enum class State {

        /**
         * The rod has been cast and the hook is flying in the air across the
         * water.
         */
        FLYING,

        /**
         * The hook has hooked an entity.
         */
        HOOKED,

        /**
         * The hook is bobbing up and down waiting for an entity.
         */
        BOBBING;
    }
}

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
package org.kryptonmc.api.block

/**
 * The reaction something will have when it is pushed.
 */
public enum class PushReaction {

    /**
     * The block will be moved by the piston as normal, and will not change.
     */
    NORMAL,

    /**
     * The block being pushed is not strong enough for this cruel world, and
     * will be destroyed when pushed.
     */
    DESTROY,

    /**
     * The block being pushed is too strong for a mere piston to move it, and
     * it blocks all attempts made by pistons to push it.
     */
    BLOCK,

    /**
     * The block will ignore any attempts made by the piston to interact with
     * it.
     */
    IGNORE,

    /**
     * The block can be pushed as normal by pistons, but no piston wields the
     * strength required to pull it back towards itself.
     */
    PUSH_ONLY
}

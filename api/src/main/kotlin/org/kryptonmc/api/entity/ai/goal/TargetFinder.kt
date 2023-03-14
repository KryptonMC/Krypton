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
package org.kryptonmc.api.entity.ai.goal

import org.kryptonmc.api.entity.Entity

/**
 * A finder for targets for an entity.
 */
public fun interface TargetFinder {

    /**
     * Finds a target for the entity.
     *
     * This must always return null if no target was found.
     *
     * @return the target, or null if no target was found
     */
    public fun findTarget(): Entity?

    /**
     * If this finder is valid and can be used to find targets.
     *
     * @return true if this finder can be used
     */
    public fun canUse(): Boolean {
        return true
    }

    /**
     * Called when this finder starts being used to find targets.
     *
     * This allows the finder to set up any state it may need before it is
     * asked to find targets.
     */
    public fun onStartUsing() {
        // Do nothing by default
    }

    /**
     * If this finder should be removed from the finders that can be used to
     * find targets, indicating it is no longer valid and should not be used.
     *
     * @return true if this finder should be removed
     */
    public fun shouldRemove(): Boolean {
        return false
    }

    /**
     * Called when this finder stops being used to find targets.
     *
     * This allows the finder to clean up any state it may have.
     */
    public fun onRemove() {
        // Do nothing by default
    }
}

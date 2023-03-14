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
 * A projectile.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Projectile : Entity {

    /**
     * The owner of this projectile, or null if this projectile does not have
     * an owner yet.
     */
    public val owner: Entity?

    /**
     * If this projectile has left its owner's hitbox.
     *
     * @return true if this projectile has left its owner
     */
    public fun hasLeftOwner(): Boolean

    /**
     * If this projectile has been shot.
     *
     * @return true if this projectile has been shot
     */
    public fun hasBeenShot(): Boolean
}

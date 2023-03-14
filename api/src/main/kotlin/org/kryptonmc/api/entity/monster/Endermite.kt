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
package org.kryptonmc.api.entity.monster

/**
 * A monster that sometimes spawns when an ender pearl lands on a block.
 */
public interface Endermite : Monster {

    /**
     * The lifetime, in ticks, of this endermite.
     *
     * This is the total amount of time until it is removed, not how long left
     * it has until it is removed. For that, see [remainingLife].
     *
     * If this endermite is made [persistent][isPersistent], it will never
     * despawn, and this lifetime will be ignored.
     */
    public var life: Int

    /**
     * The remaining lifetime, in ticks, of this endermite.
     *
     * This is the amount of time remaining until this endermite is removed.
     */
    public val remainingLife: Int
}

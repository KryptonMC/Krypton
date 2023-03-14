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
package org.kryptonmc.api.effect.sound

import net.kyori.adventure.sound.Sound
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A type of sound.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(SoundEvents::class)
@ImmutableType
public interface SoundEvent : Sound.Type {

    /**
     * The range that this sound can be heard from.
     *
     * A value of 0 indicates the sound does not have a range.
     */
    @get:JvmName("range")
    public val range: Float
}

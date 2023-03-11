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
package org.kryptonmc.api.adventure

import com.mojang.brigadier.Message
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * A Brigadier [Message] that wraps a [Component].
 *
 * This class is special, as it should be is checked for internally by the
 * command manager when a command syntax exception is thrown, so that the
 * wrapped component is correctly serialized in to JSON.
 *
 * You should use this class when you want to send a [Component] error message as
 * a response to a Brigadier command.
 */
@ImmutableType
public interface AdventureMessage : Message, ComponentLike {

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun of(component: Component): AdventureMessage
    }

    public companion object {

        /**
         * Creates a new Brigadier message that wraps the given [component].
         *
         * @param component the component message
         * @return a new adventure message
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(component: Component): AdventureMessage = Krypton.factory<Factory>().of(component)
    }
}

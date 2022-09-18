/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.adventure

import com.mojang.brigadier.Message
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton

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
@Suppress("INAPPLICABLE_JVM_NAME")
public interface AdventureMessage : Message, ComponentLike {

    @ApiStatus.Internal
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

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.adventure

import com.mojang.brigadier.Message
import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract

/**
 * A Brigadier [Message] that wraps a [Component].
 *
 * This class is special, as it should be is checked for internally by the command
 * manager when a command syntax exception is thrown, so that the [wrapped]
 * component is correctly serialized in to JSON.
 *
 * You should use this class when you want to send a [Component] error message as
 * a response to a Brigadier command.
 */
@JvmRecord
public data class AdventureMessage(public val wrapped: Component) : Message {

    override fun getString(): String = wrapped.toPlainText()
}

/**
 * Converts this component to a wrapper for a Brigadier [Message].
 */
@JvmSynthetic
@Contract("_ -> new", pure = true)
public fun Component.toMessage(): AdventureMessage = AdventureMessage(this)

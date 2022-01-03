/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.adventure

import com.mojang.brigadier.Message
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.jetbrains.annotations.Contract

/**
 * Converts this [Component] to its JSON representation, as a string.
 *
 * Analogous with [GsonComponentSerializer.serialize] called with this
 * as its argument.
 */
@JvmSynthetic
public fun Component.toJson(): String = GsonComponentSerializer.gson().serialize(this)

/**
 * Converts this [Component] to its legacy section text representation, using the
 * [section sign][LegacyComponentSerializer.SECTION_CHAR] as the translation
 * character for the legacy text.
 *
 * Analogous with [LegacyComponentSerializer.serialize] called on the
 * [section serializer][LegacyComponentSerializer.legacySection].
 */
@JvmSynthetic
public fun Component.toLegacySectionText(): String = LegacyComponentSerializer.legacySection().serialize(this)

/**
 * Converts this [Component] to its legacy ampersand text representation, using the
 * [section sign][LegacyComponentSerializer.AMPERSAND_CHAR] as the translation
 * character for the legacy text.
 *
 * Analogous with [LegacyComponentSerializer.serialize] called on the
 * [section serializer][LegacyComponentSerializer.legacyAmpersand].
 */
@JvmSynthetic
public fun Component.toLegacyAmpersandText(): String = LegacyComponentSerializer.legacyAmpersand().serialize(this)

/**
 * Converts this [Component] to its plain text representation.
 *
 * Analogous with [PlainTextComponentSerializer.serialize] called with this as
 * its argument.
 */
@JvmSynthetic
public fun Component.toPlainText(): String = PlainTextComponentSerializer.plainText().serialize(this)

/**
 * Converts this component to a wrapper for a Brigadier [Message].
 */
@JvmSynthetic
@Contract("_ -> new", pure = true)
public fun Component.toMessage(): AdventureMessage = AdventureMessage.of(this)

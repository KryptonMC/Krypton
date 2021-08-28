/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.adventure

import com.google.gson.JsonElement
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

/**
 * Converts this [Component] to a [JsonElement].
 *
 * Analogous with [GsonComponentSerializer.serializeToTree] called with this
 * as its argument.
 */
@JvmSynthetic
public fun Component.toJson(): JsonElement = GsonComponentSerializer.gson().serializeToTree(this)

/**
 * Converts this [JsonElement] to a [Component].
 *
 * Analogous with [GsonComponentSerializer.deserializeFromTree] called with this
 * as its argument.
 */
@JvmSynthetic
public fun JsonElement.toComponent(): Component = GsonComponentSerializer.gson().deserializeFromTree(this)

/**
 * Converts this [Component] to its JSON representation, as a string.
 *
 * Analogous with [GsonComponentSerializer.serialize] called with this
 * as its argument.
 */
@JvmSynthetic
public fun Component.toJsonString(): String = GsonComponentSerializer.gson().serialize(this)

/**
 * Converts this [Component] to its legacy text representation, using the given
 * [char] as the translation character for the legacy text.
 *
 * Analogous with [LegacyComponentSerializer.serialize] called with this as its
 * argument.
 */
@JvmSynthetic
public fun Component.toLegacyText(char: Char): String = LegacyComponentSerializer.legacy(char).serialize(this)

/**
 * Converts this [Component] to its legacy section text representation, using the
 * [section sign][LegacyComponentSerializer.SECTION_CHAR] as the translation character
 * for the legacy text.
 *
 * Analogous with [LegacyComponentSerializer.serialize] called on the
 * [section serializer][LegacyComponentSerializer.legacySection].
 */
@JvmSynthetic
public fun Component.toLegacySectionText(): String = LegacyComponentSerializer.legacySection().serialize(this)

/**
 * Converts this [Component] to its legacy ampersand text representation, using the
 * [section sign][LegacyComponentSerializer.AMPERSAND_CHAR] as the translation character
 * for the legacy text.
 *
 * Analogous with [LegacyComponentSerializer.serialize] called on the
 * [section serializer][LegacyComponentSerializer.legacyAmpersand].
 */
@JvmSynthetic
public fun Component.toLegacyAmpersandText(): String = LegacyComponentSerializer.legacyAmpersand().serialize(this)

/**
 * Converts this [Component] to its plain text representation.
 *
 * Analogous with [PlainTextComponentSerializer.serialize] called with this
 * as its argument.
 */
@JvmSynthetic
public fun Component.toPlainText(): String = PlainTextComponentSerializer.plainText().serialize(this)

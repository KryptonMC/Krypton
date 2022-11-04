/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
@file:Suppress("MatchingDeclarationName")
package org.kryptonmc.api.command

import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class CommandMetaDsl

/**
 * Constructs new [CommandMeta] with the given [name] and [builder] function.
 *
 * @param name the name
 * @param builder the builder
 * @return new command metadata
 */
@CommandMetaDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun commandMeta(name: String, builder: CommandMeta.Builder.() -> Unit): CommandMeta =
    CommandMeta.builder(name).apply(builder).build()

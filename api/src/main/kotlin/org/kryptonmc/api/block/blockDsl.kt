/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
@file:Suppress("MatchingDeclarationName")
package org.kryptonmc.api.block

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class BlockDsl

/**
 * Creates a new block with the given [key], [id], [stateId], and the result
 * of applying the given [builder] function.
 *
 * @param key the key
 * @param id the block ID
 * @param stateId the block state ID
 * @param builder the builder function to apply
 * @return a new block
 */
@BlockDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun block(
    key: Key,
    id: Int,
    stateId: Int = id,
    builder: Block.Builder.() -> Unit
): Block = Block.builder(key, id, stateId).apply(builder).build()

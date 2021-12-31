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
package org.kryptonmc.api.fluid

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class FluidDsl

/**
 * Creates a new fluid with the given [key], [id], [stateId], and the result
 * of applying the given [builder] function.
 *
 * @param key the key
 * @param id the fluid ID
 * @param stateId the fluid state ID
 * @param builder the builder function to apply
 * @return a new fluid
 */
@FluidDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun fluid(
    key: Key,
    id: Int,
    stateId: Int = id,
    builder: Fluid.Builder.() -> Unit
): Fluid = Fluid.builder(key, id, stateId).apply(builder).build()

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
package org.kryptonmc.api.item.data

import org.jetbrains.annotations.Contract

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class FireworkEffectDsl

/**
 * Creates a new firework effect with the given [type] and the result of
 * applying the given [builder] function.
 *
 * @param type the type
 * @param builder the builder function to apply
 * @return a new firework effect
 */
@FireworkEffectDsl
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun fireworkEffect(type: FireworkEffectType, builder: FireworkEffect.Builder.() -> Unit): FireworkEffect =
    FireworkEffect.builder(type).apply(builder).build()

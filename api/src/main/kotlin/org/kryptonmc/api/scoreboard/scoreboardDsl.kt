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
package org.kryptonmc.api.scoreboard

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.scoreboard.criteria.Criterion

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class ScoreboardDsl

/**
 * Creates a new scoreboard from the result of applying the given [builder]
 * function.
 *
 * @param builder the builder function to apply
 * @return a new scoreboard
 */
@ScoreboardDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun scoreboard(builder: Scoreboard.Builder.() -> Unit): Scoreboard = Scoreboard.builder().apply(builder).build()

/**
 * Creates a new objective with the given [name], [criterion], and the result
 * of applying the given [builder] function.
 *
 * @param name the name
 * @param criterion the criterion
 * @param builder the builder function to apply
 * @return a new objective
 */
@ScoreboardDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun objective(
    name: String,
    criterion: Criterion,
    builder: Objective.Builder.() -> Unit
): Objective = Objective.builder(name, criterion).apply(builder).build()

/**
 * Creates a new team with the given [name] and the result of applying the
 * given [builder] function.
 *
 * @param name the name
 * @param builder the builder function to apply
 * @return a new team
 */
@ScoreboardDsl
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun team(
    name: String,
    builder: Team.Builder.() -> Unit
): Team = Team.builder(name).apply(builder).build()

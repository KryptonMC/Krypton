/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

/**
 * A mob that has an age, meaning it also has a baby form.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Ageable : Mob {

    /**
     * The age of this ageable mob.
     */
    @get:JvmName("age")
    public var age: Int

    /**
     * Whether this ageable mob can naturally breed with others of its kind.
     */
    @get:JvmName("canBreedNaturally")
    public val canBreedNaturally: Boolean

    /**
     * Increases the age of this mob by the given [amount].
     *
     * @param amount the amount
     */
    public fun age(amount: Int)
}

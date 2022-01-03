/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.entity.animal.type.FoxType
import java.util.UUID

/**
 * A fox.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Fox : Animal {

    /**
     * The fox type of this fox.
     */
    @get:JvmName("foxType")
    public var foxType: FoxType

    /**
     * If this fox is currently sitting.
     */
    public var isSitting: Boolean

    /**
     * If this fox is currently crouching.
     */
    public var isCrouching: Boolean

    /**
     * If this fox is currently interested in something.
     */
    public var isInterested: Boolean

    /**
     * If this fox is currently pouncing at something.
     */
    public var isPouncing: Boolean

    /**
     * If this fox is currently sleeping.
     */
    public var isSleeping: Boolean

    /**
     * If this fox has faceplanted.
     */
    @get:JvmName("hasFaceplanted")
    public var hasFaceplanted: Boolean

    /**
     * If this fox is currently defending.
     */
    public var isDefending: Boolean

    /**
     * The first UUID that this fox trusts.
     */
    @get:JvmName("firstTrusted")
    public var firstTrusted: UUID?

    /**
     * The second UUID that this fox trusts.
     */
    @get:JvmName("secondTrusted")
    public var secondTrusted: UUID?

    /**
     * Checks if this fox trusts the given [uuid].
     *
     * This will first check if the UUID is equal to the
     * [first trusted UUID][firstTrusted], and then if it is equal to the
     * [second trusted UUID][secondTrusted].
     *
     * @param uuid the UUID
     * @return true if the UUID is trusted by this fox, false otherwise
     */
    public fun trusts(uuid: UUID): Boolean
}

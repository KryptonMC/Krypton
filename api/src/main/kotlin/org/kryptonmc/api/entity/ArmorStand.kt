/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import org.kryptonmc.api.util.Rotations

/**
 * An armor stand.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ArmorStand : LivingEntity, Equipable {

    /**
     * If this armor stand is small.
     */
    public var isSmall: Boolean

    /**
     * If this armor stand has arms.
     */
    @get:JvmName("hasArms")
    public var hasArms: Boolean

    /**
     * If this armor stand has a base plate.
     */
    @get:JvmName("hasBasePlate")
    public var hasBasePlate: Boolean

    /**
     * If this armor stand is a marker.
     */
    public var isMarker: Boolean

    /**
     * The pose this stand's head is currently making.
     */
    public var headPose: Rotations

    /**
     * The pose this stand's body is currently making.
     */
    public var bodyPose: Rotations

    /**
     * The pose this stand's left arm is currently making.
     */
    public var leftArmPose: Rotations

    /**
     * The pose this stand's right arm is currently making.
     */
    public var rightArmPose: Rotations

    /**
     * The pose this stand's left leg is currently making.
     */
    public var leftLegPose: Rotations

    /**
     * The pose this stand's right leg is currently making.
     */
    public var rightLegPose: Rotations
}

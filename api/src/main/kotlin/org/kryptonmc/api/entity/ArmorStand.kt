/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import org.kryptonmc.api.space.Rotation

/**
 * An armor stand.
 */
interface ArmorStand : LivingEntity {

    /**
     * If this armor stand is small.
     */
    var isSmall: Boolean

    /**
     * If this armor stand has arms.
     */
    var hasArms: Boolean

    /**
     * If this armor stand has a base plate.
     */
    var hasBasePlate: Boolean

    /**
     * If this armor stand is a marker.
     */
    var isMarker: Boolean

    /**
     * The pose this stand's head is currently making.
     */
    var headPose: Rotation

    /**
     * The pose this stand's body is currently making.
     */
    var bodyPose: Rotation

    /**
     * The pose this stand's left arm is currently making.
     */
    var leftArmPose: Rotation

    /**
     * The pose this stand's right arm is currently making.
     */
    var rightArmPose: Rotation

    /**
     * The pose this stand's left leg is currently making.
     */
    var leftLegPose: Rotation

    /**
     * The pose this stand's right leg is currently making.
     */
    var rightLegPose: Rotation
}

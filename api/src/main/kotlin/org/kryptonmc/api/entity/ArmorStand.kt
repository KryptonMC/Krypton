/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.entity

import org.kryptonmc.api.util.Rotation

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
    public var headPose: Rotation

    /**
     * The pose this stand's body is currently making.
     */
    public var bodyPose: Rotation

    /**
     * The pose this stand's left arm is currently making.
     */
    public var leftArmPose: Rotation

    /**
     * The pose this stand's right arm is currently making.
     */
    public var rightArmPose: Rotation

    /**
     * The pose this stand's left leg is currently making.
     */
    public var leftLegPose: Rotation

    /**
     * The pose this stand's right leg is currently making.
     */
    public var rightLegPose: Rotation
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

import org.kryptonmc.api.util.CataloguedBy
import javax.annotation.concurrent.Immutable

/**
 * A type of attribute that only accepts values between a minimum and maximum
 * value.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(AttributeTypes::class)
@Immutable
public interface RangedAttributeType : AttributeType {

    /**
     * The minimum value for attributes of this type.
     */
    @get:JvmName("minimum")
    public val minimum: Double

    /**
     * The maximum value for attributes of this type.
     */
    @get:JvmName("maximum")
    public val maximum: Double
}

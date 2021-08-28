/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

import java.util.UUID

/**
 * A modifier that can be applied to an [Attribute].
 *
 * @param name the name of the modifier
 * @param uuid the unique ID of the modifier
 * @param amount the amount of the modifier
 */
public data class AttributeModifier(
    public val name: String,
    public val uuid: UUID,
    public val amount: Double
)

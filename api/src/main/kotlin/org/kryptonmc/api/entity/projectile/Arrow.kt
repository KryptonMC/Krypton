/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.projectile

import java.awt.Color

/**
 * An arrow or tipped arrow.
 */
public interface Arrow : ArrowLike {

    /**
     * The colour of this arrow, or null if this arrow does not have a colour.
     */
    public var color: Color?
}

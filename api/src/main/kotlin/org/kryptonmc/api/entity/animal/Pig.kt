/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

/**
 * A pig.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Pig : Animal {

    /**
     * If this pig has a saddle, and can be ridden.
     */
    public var isSaddled: Boolean
}

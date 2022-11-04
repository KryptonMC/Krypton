/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity.container

import org.kryptonmc.api.block.entity.NameableBlockEntity

/**
 * A chest.
 */
public interface Chest : ContainerBlockEntity, NameableBlockEntity {

    /**
     * The chest that this chest is connected to, if this chest is a double
     * chest.
     */
    public val connectedChest: Chest?
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.Component
import org.kryptonmc.api.util.CataloguedBy
import javax.annotation.concurrent.Immutable

/**
 * A type of inventory.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(InventoryTypes::class)
@Immutable
public interface InventoryType : Keyed {

    /**
     * The size of the inventory.
     */
    @get:JvmName("size")
    public val size: Int

    /**
     * The default title that will be displayed when inventories of this type
     * are sent and no custom title has been defined.
     */
    @get:JvmName("defaultTitle")
    public val defaultTitle: Component
}

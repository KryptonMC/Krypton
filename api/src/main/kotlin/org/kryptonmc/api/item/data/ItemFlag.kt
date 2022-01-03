/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.data

/**
 * A flag that may be applied to an item to hide something that would usually
 * display on it, such as its enchantments.
 */
public enum class ItemFlag {

    HIDE_ENCHANTMENTS,
    HIDE_ATTRIBUTES,
    HIDE_UNBREAKABLE,
    HIDE_CAN_DESTROY,
    HIDE_CAN_PLACE,
    HIDE_MISCELLANEOUS,
    HIDE_DYE
}

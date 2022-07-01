/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

/**
 * A slot that a piece of equipment may be in.
 *
 * @param type the type of equipment
 */
public enum class EquipmentSlot(public val type: Type) {

    MAIN_HAND(Type.HAND),
    OFF_HAND(Type.HAND),
    FEET(Type.ARMOR),
    LEGS(Type.ARMOR),
    CHEST(Type.ARMOR),
    HEAD(Type.ARMOR);

    /**
     * A type of equipment.
     */
    public enum class Type {

        HAND,
        ARMOR
    }
}

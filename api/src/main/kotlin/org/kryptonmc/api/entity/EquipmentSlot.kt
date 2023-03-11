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

/**
 * A slot that a piece of equipment may be in.
 *
 * @property type The type of equipment that this slot is for.
 */
public enum class EquipmentSlot(public val type: Type) {

    MAIN_HAND(Type.HAND),
    OFF_HAND(Type.HAND),
    FEET(Type.ARMOR),
    LEGS(Type.ARMOR),
    CHEST(Type.ARMOR),
    HEAD(Type.ARMOR);

    /**
     * A type of equipment slot.
     */
    public enum class Type {

        HAND,
        ARMOR
    }
}

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
package org.kryptonmc.api.block.meta

/**
 * Indicates the side of the door that the hinges will appear on, and the
 * direction the door will open in when it is opened.
 */
public enum class DoorHingeSide {

    /**
     * The hinges will appear on the left side of the door, and the door will
     * open to the left.
     */
    LEFT,

    /**
     * The hinges will appear on the right side of the door, and the door will
     * open to the right.
     */
    RIGHT
}

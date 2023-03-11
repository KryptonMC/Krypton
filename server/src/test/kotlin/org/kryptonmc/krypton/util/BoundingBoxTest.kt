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
package org.kryptonmc.krypton.util

import org.junit.jupiter.api.Test
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Vec3d
import kotlin.test.assertEquals

class BoundingBoxTest {

    @Test
    fun `test size of box calculations`() {
        val box = BoundingBox(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0)
        assertEquals(6.0, box.totalSize())
    }

    @Test
    fun `test volume calculation`() {
        val box = BoundingBox(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0)
        val expectedVolume = 6.0 * 6.0 * 6.0 // size.x * size.y * size.z
        assertEquals(expectedVolume, box.volume())
    }

    @Test
    fun `ensure center of box with negative and equal minimum values and equal maximum values is zero`() {
        val box = BoundingBox(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0)
        assertEquals(Vec3d.ZERO, box.center())
    }
}

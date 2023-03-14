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
package org.kryptonmc.krypton.adventure

import net.kyori.adventure.text.format.NamedTextColor
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.util.Reflection
import kotlin.test.assertEquals

/*
 * The tests in this class ensure things in the internals do not break, to avoid us becoming incompatible after an update.
 */
class AdventureCompatibilityTest {

    @Test
    fun `verify ids do not break on update`() {
        // TODO: Change the internals of `getColorId` to stop using reflection, so this test actually means something.
        // The problem with this test is that the internals use this values list, so we won't end up testing anything.
        val values = Reflection.accessField<NamedTextColor, List<NamedTextColor>>("VALUES")
        values.forEachIndexed { index, element -> assertEquals(index, KryptonAdventure.getColorId(element)) }
    }
}

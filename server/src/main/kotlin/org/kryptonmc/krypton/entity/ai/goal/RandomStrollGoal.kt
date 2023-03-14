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
package org.kryptonmc.krypton.entity.ai.goal

import org.kryptonmc.api.entity.ai.goal.Goal
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.util.random.RandomSource

class RandomStrollGoal(private val entity: KryptonMob, private val radius: Int) : Goal {

    private val positions = getBlocksInRange(radius)
    private val random = RandomSource.create()

    private var lastStroll = 0L

    override fun canUse(): Boolean = System.currentTimeMillis() - lastStroll >= DELAY

    override fun start() {
        var remainingAttempt = positions.size
        while (remainingAttempt-- > 0) {
            val index = random.nextInt(positions.size)
            val position = positions.get(index)
            val target = entity.position.add(position)
            if (entity.navigator.tryPathTo(target.asVec3d())) break
        }
    }

    override fun tick(time: Long) {
        // Nothing to do
    }

    override fun shouldStop(): Boolean = entity.navigator.hasReachedTarget()

    override fun stop() {
        lastStroll = System.currentTimeMillis()
    }

    private fun getBlocksInRange(radius: Int): List<Vec3i> {
        val result = ArrayList<Vec3i>()
        for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    result.add(Vec3i(x, y, z))
                }
            }
        }
        return result
    }

    companion object {

        private const val DELAY = 2500
    }
}

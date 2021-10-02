/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.fluid

import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i
import java.util.Random

/**
 * A handler for one (or multiple) [Fluid]s.
 */
public interface FluidHandler {

    /**
     * Gets the movement vector for the current flow of the given [fluid] at
     * the given [position] in the given [world].
     *
     * @param fluid the fluid to get the flow of
     * @param position the position of the fluid
     * @param world the world the fluid is in
     * @return the flow movement of the fluid
     */
    public fun getFlow(fluid: Fluid, position: Vector3i, world: World): Vector3d

    /**
     * Gets the height of the given [fluid] at the given [position] in the
     * given [world].
     *
     * @param fluid the fluid to get the height of
     * @param position the position of the fluid
     * @param world the world the fluid is in
     * @return the height of the fluid
     */
    public fun getHeight(fluid: Fluid, position: Vector3i, world: World): Float

    /**
     * Returns true if the given [fluid] at the given [position] in the given
     * [world] facing the given [direction] can be replaced with the given
     * [replacement] fluid, false otherwise.
     *
     * @param fluid the current fluid
     * @param position the position of the fluid
     * @param world the world the fluid is in
     * @param direction the direction the fluid is facing
     * @param replacement the fluid that could replace the current fluid
     * @return true if the fluid can be replaced with the replacement, false
     * otherwise
     */
    public fun canReplaceWith(
        fluid: Fluid,
        position: Vector3i,
        world: World,
        direction: Direction,
        replacement: Fluid
    ): Boolean

    /**
     * Called when the given [fluid] is ticked at the given [position] in the
     * given [world].
     *
     * @param fluid the fluid being ticket
     * @param position the position the fluid is at
     * @param world the world the fluid is in
     */
    public fun tick(fluid: Fluid, position: Vector3i, world: World)

    /**
     * Called when the given [fluid] is randomly ticked at the given [position]
     * in the given [world] with the given [random] number generator.
     *
     * @param fluid the fluid being ticket
     * @param position the position the fluid is at
     * @param world the world the fluid is in
     * @param random the random number generator to use to generate randomness
     * for the tick
     */
    public fun randomTick(fluid: Fluid, position: Vector3i, world: World, random: Random)
}

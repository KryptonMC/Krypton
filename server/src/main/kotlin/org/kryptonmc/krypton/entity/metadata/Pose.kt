package org.kryptonmc.krypton.entity.metadata

/**
 * A pose that an entity may be in.
 *
 * @author Callum Seabrook
 */
enum class Pose(val id: Int) {

    STANDING(0),
    FALL_FLYING(1),
    SLEEPING(2),
    SWIMMING(3),
    SPIN_ATTACK(4),
    SNEAKING(5),
    DYING(6)
}
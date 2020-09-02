package me.bristermitten.minekraft.entities.block.actions.impl

import me.bristermitten.minekraft.entities.block.actions.BlockAction

enum class PistonBlockAction(
    override val actionID: PistonAction,
    override val actionValue: PistonDirection
) : BlockAction<PistonAction, PistonDirection> {

    EXTEND_UP(PistonAction.EXTEND, PistonDirection.UP),
    EXTEND_DOWN(PistonAction.EXTEND, PistonDirection.DOWN),
    EXTEND_NORTH(PistonAction.EXTEND, PistonDirection.NORTH),
    EXTEND_SOUTH(PistonAction.EXTEND, PistonDirection.SOUTH),
    EXTEND_EAST(PistonAction.EXTEND, PistonDirection.EAST),
    EXTEND_WEST(PistonAction.EXTEND, PistonDirection.WEST),

    RETRACT_UP(PistonAction.RETRACT, PistonDirection.UP),
    RETRACT_DOWN(PistonAction.RETRACT, PistonDirection.DOWN),
    RETRACT_NORTH(PistonAction.RETRACT, PistonDirection.NORTH),
    RETRACT_SOUTH(PistonAction.RETRACT, PistonDirection.SOUTH),
    RETRACT_EAST(PistonAction.RETRACT, PistonDirection.EAST),
    RETRACT_WEST(PistonAction.RETRACT, PistonDirection.WEST)
}

enum class PistonAction(
    override val id: Int
) : BlockAction.ActionID {

    EXTEND(0),
    RETRACT(1)
}

enum class PistonDirection(
    override val id: Int
) : BlockAction.ActionParameter {

    UP(1),
    DOWN(0),
    NORTH(4),
    SOUTH(2),
    EAST(5),
    WEST(3)
}
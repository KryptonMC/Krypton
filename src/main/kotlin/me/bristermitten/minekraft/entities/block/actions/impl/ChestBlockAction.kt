package me.bristermitten.minekraft.entities.block.actions.impl

import me.bristermitten.minekraft.entities.block.actions.BlockAction

data class ChestBlockAction(
    override val actionID: ChestAction,
    override val actionValue: ChestPlayers
) : BlockAction<ChestAction, ChestPlayers>

enum class ChestAction(override val id: Int) : BlockAction.ActionID {

    UPDATE_PLAYERS(1)
}

// In this case, ID is the amount of players when ChestAction is OPEN, or 0 if CLOSED
data class ChestPlayers(override val id: Int) : BlockAction.ActionParameter
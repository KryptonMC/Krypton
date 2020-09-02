package me.bristermitten.minekraft.entities.block.actions.impl

import me.bristermitten.minekraft.entities.block.actions.BlockAction

data class ShulkerBoxBlockAction(
    override val actionID: ShulkerBoxAction,
    override val actionValue: ShulkerBoxPlayers
) : BlockAction<ShulkerBoxAction, ShulkerBoxPlayers>

enum class ShulkerBoxAction(override val id: Int) : BlockAction.ActionID {

    UPDATE_PLAYERS(1)
}

data class ShulkerBoxPlayers(override val id: Int) : BlockAction.ActionParameter
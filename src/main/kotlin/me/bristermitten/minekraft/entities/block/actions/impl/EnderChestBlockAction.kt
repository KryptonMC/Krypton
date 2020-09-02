package me.bristermitten.minekraft.entities.block.actions.impl

import me.bristermitten.minekraft.entities.block.actions.BlockAction

// The only reason this exists is because the chest block action and this ender chest block action
// are classed as different actions that are updated and sent differently
data class EnderChestBlockAction(
    override val actionID: EnderChestAction,
    override val actionValue: EnderChestPlayers
) : BlockAction<EnderChestAction, EnderChestPlayers>

enum class EnderChestAction(override val id: Int) : BlockAction.ActionID {

    UPDATE_PLAYERS(1)
}

// Same as regular chest
data class EnderChestPlayers(override val id: Int) : BlockAction.ActionParameter
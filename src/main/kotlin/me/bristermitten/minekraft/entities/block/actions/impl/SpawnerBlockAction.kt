package me.bristermitten.minekraft.entities.block.actions.impl

import me.bristermitten.minekraft.entities.block.actions.BlockAction

enum class SpawnerBlockAction(
    override val actionID: SpawnerAction
) : BlockAction<SpawnerAction, Nothing?> {

    RESET_SPAWN_DELAY(SpawnerAction.RESET_SPAWN_DELAY)
}

enum class SpawnerAction(override val id: Int) : BlockAction.ActionID {

    RESET_SPAWN_DELAY(1)
}
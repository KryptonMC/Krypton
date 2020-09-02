package me.bristermitten.minekraft.entities.block.actions.impl

import me.bristermitten.minekraft.entities.block.actions.BlockAction

enum class BeaconBlockAction(
    override val actionID: BeaconAction
) : BlockAction<BeaconAction, Nothing?> {

    UPDATE_BEAM(BeaconAction.UPDATE_BEAM)
}

enum class BeaconAction(override val id: Int) : BlockAction.ActionID {

    UPDATE_BEAM(1)
}
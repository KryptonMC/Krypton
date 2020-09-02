package me.bristermitten.minekraft.entities.block.actions.impl

import me.bristermitten.minekraft.entities.block.actions.BlockAction

enum class EndGatewayBlockAction(
    override val actionID: EndGatewayAction
) : BlockAction<EndGatewayAction, Nothing?> {

    TRIGGER_BEAM(EndGatewayAction.TRIGGER_BEAM)
}

enum class EndGatewayAction(override val id: Int) : BlockAction.ActionID {

    TRIGGER_BEAM(1)
}
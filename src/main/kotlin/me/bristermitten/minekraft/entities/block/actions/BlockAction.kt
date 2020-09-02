package me.bristermitten.minekraft.entities.block.actions

import me.bristermitten.minekraft.entities.block.actions.BlockAction.ActionID
import me.bristermitten.minekraft.entities.block.actions.BlockAction.ActionParameter

interface BlockAction<T : ActionID?, U : ActionParameter?> {

    val actionID: T?
        get() = null

    val actionValue: U?
        get() = null

    interface ActionID {

        val id: Int
    }

    interface ActionParameter {

        val id: Int
    }
}
package org.kryptonmc.krypton.event.player

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.player.EntityExitViewEvent

class KryptonEntityExitViewEvent(override val player: Player, override val entity: Entity) : EntityExitViewEvent

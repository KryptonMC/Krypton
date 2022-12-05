package org.kryptonmc.krypton.testutil

import org.kryptonmc.krypton.entity.components.NetworkPlayer
import org.kryptonmc.krypton.entity.player.PlayerPublicKey

class TestNetworkPlayer(override val session: TestSession) : NetworkPlayer {

    override val publicKey: PlayerPublicKey?
        get() = null
}

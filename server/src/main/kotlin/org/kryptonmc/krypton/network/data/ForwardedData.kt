package org.kryptonmc.krypton.network.data

import org.kryptonmc.krypton.auth.KryptonProfileProperty
import java.util.UUID

interface ForwardedData {

    val forwardedAddress: String
    val forwardedPort: Int

    val uuid: UUID?
        get() = null
    val properties: List<KryptonProfileProperty>?
        get() = null
}

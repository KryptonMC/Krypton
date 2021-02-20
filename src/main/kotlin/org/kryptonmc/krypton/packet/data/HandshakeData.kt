package org.kryptonmc.krypton.packet.data

import org.kryptonmc.krypton.packet.state.PacketState
import java.net.InetAddress

data class HandshakeData(
	val protocol: Int,
	val address: InetAddress,
	val port: Short,
	val nextState: PacketState
)
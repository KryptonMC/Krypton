package me.bristermitten.minekraft.packet.data

import me.bristermitten.minekraft.packet.state.PacketState
import java.net.InetAddress

data class HandshakeData(
	val protocol: Int,
	val address: InetAddress,
	val port: Short,
	val nextState: PacketState
)


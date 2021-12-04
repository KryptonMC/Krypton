package org.kryptonmc.krypton.network.data

@JvmRecord
data class TCPShieldForwardedData(
    override val forwardedAddress: String,
    override val forwardedPort: Int,
    val timestamp: Int,
    val signature: String
) : ForwardedData {

    companion object {

        @JvmStatic
        fun parse(string: String): TCPShieldForwardedData? {
            val split = string.split("///")
            if (split.size < 4) return null
            val ipData = split[1].split(':')
            if (ipData.size != 2) return null

            val ip = ipData[0]
            val port = ipData[1].toIntOrNull() ?: return null
            val timestamp = split[2].toIntOrNull() ?: return null
            val signature = split[3]
            return TCPShieldForwardedData(ip, port, timestamp, signature)
        }
    }
}

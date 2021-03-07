package org.kryptonmc.krypton.packet.out.play.chat

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.extension.writeDuration
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import java.time.Duration

class PacketOutTitle(
    private val action: TitleAction,
    private val title: Title,
    private val actionBar: Component = Component.empty()
) : PlayPacket(0x4F) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.id)

        when (action) {
            TitleAction.SET_TITLE -> buf.writeChat(title.title())
            TitleAction.SET_SUBTITLE -> buf.writeChat(title.subtitle())
            TitleAction.SET_ACTION_BAR -> buf.writeChat(actionBar)
            TitleAction.SET_TIMES_AND_DISPLAY -> {
                buf.writeDuration(title.times()?.fadeIn() ?: Duration.ZERO)
                buf.writeDuration(title.times()?.stay() ?: Duration.ZERO)
                buf.writeDuration(title.times()?.fadeOut() ?: Duration.ZERO)
            }
            else -> Unit
        }
    }
}

enum class TitleAction(val id: Int) {

    SET_TITLE(0),
    SET_SUBTITLE(1),
    SET_ACTION_BAR(2),
    SET_TIMES_AND_DISPLAY(3),
    HIDE(4),
    RESET(5)
}
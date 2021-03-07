package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeKey
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.NamespacedKey

// TODO: Use this
class PacketOutUnlockRecipes(
    private val action: UnlockRecipesAction,
    private val isCraftingBookOpen: Boolean = false,
    private val isCraftingBookFiltered: Boolean = false,
    private val isSmeltingBookOpen: Boolean = false,
    private val isSmeltingBookFiltered: Boolean = false,
    private val isBlastFurnaceBookOpen: Boolean = false,
    private val isBlastFurnaceBookFiltered: Boolean = false,
    private val isSmokerBookOpen: Boolean = false,
    private val isSmokerBookFiltered: Boolean = false,
    private val recipes: List<NamespacedKey> = emptyList(),
    private val newRecipes: List<NamespacedKey> = emptyList()
) : PlayPacket(0x35) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.id)

        buf.writeBoolean(isCraftingBookOpen)
        buf.writeBoolean(isCraftingBookFiltered)
        buf.writeBoolean(isSmeltingBookOpen)
        buf.writeBoolean(isSmeltingBookFiltered)
        buf.writeBoolean(isBlastFurnaceBookOpen)
        buf.writeBoolean(isBlastFurnaceBookFiltered)
        buf.writeBoolean(isSmokerBookOpen)
        buf.writeBoolean(isSmokerBookFiltered)

        buf.writeVarInt(recipes.size)
        for (recipe in recipes) buf.writeKey(recipe)

        if (action == UnlockRecipesAction.INIT) {
            buf.writeVarInt(newRecipes.size)
            for (recipe in newRecipes) buf.writeKey(recipe)
        }
    }
}

enum class UnlockRecipesAction(val id: Int) {

    INIT(0),
    ADD(1),
    REMOVE(2)
}
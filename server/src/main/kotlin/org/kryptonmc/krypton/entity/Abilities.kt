package org.kryptonmc.krypton.entity

data class Abilities(
    val isInvulnerable: Boolean = false,
    val canFly: Boolean = false,
    val isFlyingAllowed: Boolean = false,
    val isCreativeMode: Boolean = false,
    val flyingSpeed: Float = DEFAULT_FLYING_SPEED,
    val fieldOfViewModifier: Float = DEFAULT_FIELD_OF_VIEW_MODIFIER
) {

    fun flagsToProtocol(): Int {
        var flagsByte = 0x00
        if (isInvulnerable) flagsByte += 0x01
        if (canFly) flagsByte += 0x02
        if (isFlyingAllowed) flagsByte += 0x04
        if (isCreativeMode) flagsByte += 0x08
        return flagsByte
    }

    companion object {

        private const val DEFAULT_FLYING_SPEED = 0.05f
        private const val DEFAULT_FIELD_OF_VIEW_MODIFIER = 0.1f
    }
}